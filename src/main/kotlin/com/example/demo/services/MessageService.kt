package com.example.demo.services

import com.example.demo.dto.Message
import com.example.demo.repositories.MessageRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MessageService(
    private val db: JdbcTemplate,
    private val messageRepository: MessageRepository
) {

    fun findMessages(): List<Message> = db.query("select * from messages")
    { response, _ ->
        Message(
            id = response.getString("id"),
            text = response.getString("text")
        )
    }

    fun findMessages2(): List<Message> = messageRepository.findAll().toList()

    fun findMessageById(id: String): Message? = db.query("select * from messages where id=?", id)
    { response, _ ->
        Message(
            id = response.getString("id"),
            text = response.getString("text")
        )
    }.singleOrNull()

    fun findMessageById2(id: String): Message? = messageRepository.findByIdOrNull(id)

    fun save(message: Message): Message {
        val id = message.id ?: UUID.randomUUID().toString()
        db.update("insert into messages values (?,?)", id, message.text)
        return message.copy(id = id)
    }

    fun save2(message: Message): Message = messageRepository.save(message)
}