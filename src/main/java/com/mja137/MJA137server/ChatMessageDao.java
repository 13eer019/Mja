package com.mja137.MJA137server;

import org.springframework.data.repository.CrudRepository;

public interface ChatMessageDao extends CrudRepository<ChatMessageEntity, String> {

}
