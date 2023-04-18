# Linebot
Side project of Linebot。練習 Webhook, LineAPI, Design Pattern  
由 Line Platform 過來的資料大致區分不同 Event，根據不同 Event 在不同的 Service 去指派對應的處理 Strategy，處理後的訊息以樣板形式回傳

## 功能
- 樣板內容顯示店家資料
- 樣板內容顯示產品資料
- 店家預約功能
- 預約功能查詢/取消預約

## Server   
使用 Ngrok 部屬 linebot

## DB
使用 H2 暫存資料

## Spring
SpringBoot當作應用程式框架  
Spring-Data-JPA 當作 ORM，映射資料庫  

## LINE API
LineMessageHandler 接收 Line webhook 過來的資料，由 EventMapping 去處理相對應的資料型態

## Data Process
根據不同類型的 Event，使用 Generic Strategy 去包裝回傳訊息，回傳的訊息再丟給 Line Platform
- 處理 MessageEvent
- 處理 PostbackEvent

