-- Create table
CREATE TABLE IF NOT EXISTS "ChatMessage" (
    "Id" SERIAL PRIMARY KEY,
    "Sender" VARCHAR(255) NOT NULL,
    "Receiver" VARCHAR(255) NOT NULL,
    "Content" TEXT NOT NULL,
    "SentAt" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Function to insert a chat message (without sent_at)
CREATE OR REPLACE FUNCTION save_chat_message(
    pSender VARCHAR,
    pReceiver VARCHAR,
    pContent TEXT
)
RETURNS VOID
LANGUAGE plpgsql
AS
'
BEGIN
    INSERT INTO "ChatMessage" ("Sender", "Receiver", "Content")
    VALUES (pSender, pReceiver, pContent);
END;
';

-- Function to retrieve chat history
CREATE OR REPLACE FUNCTION get_chat_history(
    pUser1 VARCHAR,
    pUser2 VARCHAR
)
RETURNS TABLE (
    lSender VARCHAR,
    lReceiver VARCHAR,
    lContent TEXT,
    lSentAt TIMESTAMP
)
LANGUAGE plpgsql
AS
'
BEGIN
    RETURN QUERY
    SELECT 
        "Sender",
        "Receiver",
        "Content",
        "SentAt"
    FROM "ChatMessage"
    WHERE 
        ("Sender" = pUser1 AND "Receiver" = pUser2)
        OR
        ("Sender" = pUser2 AND "Receiver" = pUser1)
    ORDER BY "SentAt" ASC;

    IF NOT FOUND THEN
        RAISE EXCEPTION ''No chat messages found between these users.'';
    END IF;
END;
';