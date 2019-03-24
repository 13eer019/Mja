package com.mja137.MJA137server;

import java.util.List;

public class Greeting {
	
    private List<ChatMessageEntity> chatMessageEntity;

    public Greeting(List<ChatMessageEntity> chatList) {
		this.chatMessageEntity=chatList;
	}

	public List<ChatMessageEntity> getChatMessageEntity() {
		return chatMessageEntity;
	}

	public void setChatMessageEntity(List<ChatMessageEntity> chatMessageEntity) {
		this.chatMessageEntity = chatMessageEntity;
	}
}
