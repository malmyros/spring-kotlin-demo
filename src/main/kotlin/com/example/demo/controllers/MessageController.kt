package com.example.demo.controllers

import com.example.demo.dto.Message
import com.example.demo.services.MessageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class MessageController(private val messageService: MessageService) {

    @GetMapping("/hello")
    fun index(@RequestParam("name") name: String) = "Hello, $name!"

    @PostMapping("/")
    fun createMessage(@RequestBody message: Message): ResponseEntity<Message> {
        val savedMessage = messageService.save(message)
        return ResponseEntity
            .created(URI("/${savedMessage.id}"))
            .body(savedMessage)
    }

    @GetMapping("/")
    fun listMessages() = messageService.findMessages()

    @GetMapping("/{id}")
    fun getMessageById(@PathVariable("id") id: String): ResponseEntity<Message> {
        return messageService.findMessageById(id).toResponseEntity()
    }

    private fun Message?.toResponseEntity(): ResponseEntity<Message> =
        this?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
}