import websocket
import threading
import time
import json

class StompWebSocketClient:
    def __init__(self, url, sender_id, receiver_id):
        self.url = url
        self.sender_id = sender_id
        self.receiver_id = receiver_id

    def on_message(self, ws, message):
        print(f"📩 Received: {message}")

    def on_error(self, ws, error):
        print(f"❌ Error: {error}")

    def on_close(self, ws, close_status_code, close_msg):
        print("🔒 Connection closed")

    def on_open(self, ws):
        print("✅ Connection opened")

        # 1. CONNECT frame
        ws.send("CONNECT\naccept-version:1.2\nhost:localhost\n\n\x00")

        time.sleep(1)

        # 2. SUBSCRIBE to personal message queue
        subscribe_frame = f"SUBSCRIBE\nid:sub-0\ndestination:/user/{self.sender_id}/queue/messages\n\n\x00"
        ws.send(subscribe_frame)

        # 3. Add user
        add_user_msg = {
            "senderId": self.sender_id,
            "receiverId": self.receiver_id,
            "content": "Adding user"
        }
        send_frame = (
            f"SEND\ndestination:/app/chat.addUser\ncontent-type:application/json\n\n"
            f"{json.dumps(add_user_msg)}\x00"
        )
        ws.send(send_frame)

        # 4. Send message
        chat_msg = {
            "senderId": self.sender_id,
            "receiverId": self.receiver_id,
            "content": "Hello from Python!"
        }
        send_frame = (
            f"SEND\ndestination:/app/chat.sendMessage\ncontent-type:application/json\n\n"
            f"{json.dumps(chat_msg)}\x00"
        )
        ws.send(send_frame)

    def run(self):
        websocket.enableTrace(True)
        ws = websocket.WebSocketApp(
            self.url,
            on_open=self.on_open,
            on_message=self.on_message,
            on_error=self.on_error,
            on_close=self.on_close,
        )
        wst = threading.Thread(target=ws.run_forever)
        wst.start()


if __name__ == "__main__":
    ws_url = "ws://localhost:8080/ws/websocket"  # Spring uses SockJS fallback endpoint
    client = StompWebSocketClient(ws_url, sender_id="farmer1", receiver_id="manager2")
    client.run()
