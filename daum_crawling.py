import pymysql
import os
import sys
import time
import json
import requests
from bs4 import BeautifulSoup
import re
from urllib.request import urlopen
from datetime import datetime, timedelta
from emoji import core

from konlpy.tag import Okt, Kkma
from collections import defaultdict
import numpy as np
import networkx as nx


okt = Okt()
splt = Kkma()
ids=defaultdict(int)
countWordLimit=25
global con, cur, insertCnt, weekCnt, dupCnt

insertCnt=0
weekCnt=0
dupCnt=0


def crawling(category):
    global insertCnt, weekCnt, dupCnt
    category_url='https://news.daum.net/'+category
    response = requests.get(category_url)
    html = response.text
    soup = BeautifulSoup(html,'html.parser')
    
    news_tit_url_tags = soup.find_all('a', class_='link_txt')
    # print(news_tit_url_tags)
    # print()

    for tag in news_tit_url_tags:
        link = tag.get('href')
        if(link=="/"):
            break
        print(link)
        info = link.split('/')[-1]
        if(len(info)<8):
            continue
        date = info[:8]
        if(date[:2]!="20"):
            break
        originId = info[8:]
        if(dupSource(originId,link)): #중복췤
            dupCnt+=1
            print("중복 스킵 @@@@@@@@@@@@@@@@@@@@@@@@@@")
            continue
        #print(date, " ", originId)
        title, content = getContent(link)
        contentHead = content
        if(len(contentHead)>100):
            contentHead = contentHead[:100]
        #print(content)
        wordCountList, totalWordCnt= wordCount(content)


        if(len(wordCountList)<10 or (len(wordCountList)>=5) and wordCountList[5][1]<=3): #빈약한거 버림
            weekCnt+=1
            print(wordCountList[:5])
            print("빈약해서 스킵 ########################")
            continue
        sourceId = insertSource(originId, title, link, contentHead, date) #source insert
        if(sourceId==-1 ): 
            continue
        insertTotalWordCnt(sourceId, totalWordCnt, link)

        for(keyword, count) in wordCountList:
            insertKeyword(sourceId, keyword, count, date, link, totalWordCnt) #keyword insert
        insertCnt+=1
        print(wordCountList)
        

def getContent(link):
    response = requests.get(link)
    html = response.text
    soup = BeautifulSoup(html,'html.parser')
    title =""
    title_tags = soup.find(attrs={'class': 'tit_view'})
    if title_tags:
        title=title_tags.text
    print("제목:",title)
    content=""


    target_p_tags = soup.find_all('p', {'dmcf-ptype': 'general'})
    #print(target_p_tags)
    for target_p_tag in target_p_tags:
        content+= target_p_tag.text

    target_div_tags = soup.find_all('div', {'dmcf-ptype': 'general'})
    for target_div_tag in target_div_tags:
        if target_div_tag:
            target_p_tag = target_div_tag.find('p')
            if target_p_tag:
                content += target_p_tag.text
            else:
                content += target_div_tag.text
    
    content.strip()
    return [title,content]

    
    

def wordCount(content):
    sentences = getSummaryWordsList(content)
    #print(sentences)
    wordDict=defaultdict(int)
    for sentence in sentences:
        for word in sentence:
            wordDict[word]=wordDict[word]+1
    #print(wordDict)
    keys = sorted(wordDict, key=wordDict.get, reverse=True)[:countWordLimit]
    totalWordCnt=0
    wordCountList=[]
    for i,key in enumerate(keys):
        if(wordDict[key]==1):
            totalWordCnt+= len(keys)-i
            break
        totalWordCnt+=wordDict[key]
        wordCountList.append([key,wordDict[key]])
    return (wordCountList, totalWordCnt)


def getSummaryWordsList(text):
    sentences = sentence_tokenize(text)
    num_sent = len(sentences)
    print("문장수 :",num_sent)
    if num_sent >= 20:
        num_sent = (num_sent // 5)*4
    elif num_sent > 12:
        num_sent-=2
    
    summary = textrank(sentences, num_sent)
    return summary

def sentence_tokenize(text):
    sentences = text.split('.')
    sentences = [s.strip() for s in sentences if len(s) > 10]
    return sentences

def textrank(sentences, num_sent):
    wordsList = word_tokenize(sentences)
    if(len(wordsList) == num_sent):
        return wordsList
    #문장별 단어 set추출
    wordsSet = []
    for i in range(len(sentences)):
        wordsSet.append(set(wordsList[i]))

    # 문장 그래프 생성
    graph = np.zeros((len(sentences), len(sentences)))
    for i in range(len(sentences)-1):
        for j in range(i+1,len(sentences)):
            intersect = wordsSet[i] & wordsSet[j]
            union = wordsSet[i] | wordsSet[j]
            jaccard = len(intersect) / (len(union)+1)
            graph[i][j] = jaccard
            graph[j][i] = jaccard

    # 문장 중요도에 따라 그래프 랭킹 계산
    sentence_graph = nx.from_numpy_array(graph)
    scores = nx.pagerank(sentence_graph)
    ranked_sentences = sorted(((scores[i],i, s) for i, s in enumerate(sentences)), reverse=True)

    # 상위 num_sent 개의 문장 반환
    summary_sentences = []
    summary_wordsList=[]
    for i in range(num_sent):
        summary_wordsList.append( wordsList[ranked_sentences[i][1]] )
    
    return summary_wordsList


#형태소 분석을 통한 단어 추출
def word_tokenize(sentences):
    wordsList = []
    for sentence in sentences:
        words=[]
        for (word, tp) in okt.pos(sentence):
            if len(word)== 1 or tp=='Josa':
                continue
            if tp == 'Noun' or tp == "Alpha":
                words.append(word)
            elif tp == 'Adjective' or tp=="Verb":
                verbs = splt.pos(word)
                verb, t_class = verbs[0]
                if(t_class=="NNG" or t_class=="XR") and len(verb)>1 :
                    words.append(verb)
                elif(t_class=="MAG" and verb!="하여"):
                    words.append(verb)
                elif(t_class=="VV" or t_class=="VA"):
                    if(len(verb)==1 and(verb[0]=='하' or verb[0]=='되' or verb[0]=='있' or verb[0]=='없')):
                        continue
                    words.append(verb+"다") 
        wordsList.append(words)

    #print(wordsList)
    return wordsList

def connectDB():
    global con, cur
    con = pymysql.connect(host='localhost',port=3306, user='root', password='1160', db='trend', charset='utf8') 
    cur = con.cursor()


def insertSource(originId, title, link, contentHead, date):
    global con, cur
    insertSql = 'INSERT INTO source(origin_id, title, link, content, reg_dt) VALUES( %s, %s, %s, %s, %s)'
    try:
        cur.execute(insertSql, (originId, title, link, contentHead, date))
        con.commit()
        getSql="select source_id from source where origin_id = %s"
        cur.execute(getSql, (originId))
        sourceId = cur.fetchall()[0][0]
        
        return sourceId
    except Exception as e:
        print(originId,"source db 입력 실패^^^^^^^^^^^^^^^^^^^^^^^^^^")
        errorLog(sourceId, link, "source", e)
        return -1
    
def insertTotalWordCnt(sourceId, totalWordCnt, link):
    sql = "UPDATE source SET total_word_cnt = %s where source_id= %s"
    try :
        cur.execute(sql,( totalWordCnt, sourceId))
        con.commit()
    except Exception as e:
        print(sourceId,"totalWordCnt 실패^^^^^^^^^^^^^^^^^^^^^^^^^^")
        errorLog(sourceId, link, "twc", e)

def dupSource(originId, link):
    sql = "SELECT source_id, link FROM source WHERE origin_id = %s "
    cur.execute(sql, (originId))
    infos = cur.fetchall()
    if(len(infos)==0):
        return False
    sourceId=infos[0][0]
    linkDB=infos[0][1]
    e="ok"
    if link!=linkDB:
        e=linkDB
    errorLog(sourceId, link, "dup", e)
    return True

def errorLog(sourceId, link, tp, error):
    insertSql = 'INSERT INTO log(source_id, link, type, error) VALUES( %s, %s, %s, %s)'
    try:
         cur.execute(insertSql, (sourceId, link, tp, error))
         con.commit()
    except Exception as e:
        pass
        
def insertKeyword(sourceId, keyword, count, date, link, totalWordCnt):
    global con, cur
    importance = (count*1000)//totalWordCnt
    insertSql = 'INSERT INTO keyword(source_id, keyword, count, importance, reg_dt) VALUES( %s, %s, %s, %s, %s)'
    try:
         cur.execute(insertSql, (sourceId, keyword, count, importance, date))
         con.commit()
    except Exception as e:
        print(sourceId, "source keyword 입력 실패^^^^^^^^^^^^^^^^^^^^^^^^^^")
        errorLog(sourceId, link, "keyword", keyword+" "+e )


if __name__ == "__main__":
    connectDB()
    categorys=['society', 'politics', 'economic', 'foreign', 'culture', 'digital'] #
    for categroy in categorys:
        print(categroy," 시작=================================================")
        crawling(categroy)
        print(categroy," 끝===================================================")
    
    print()
    print(insertCnt,"개 추가 완료, ", weekCnt,"개 빈약, ",dupCnt,"개 중복")