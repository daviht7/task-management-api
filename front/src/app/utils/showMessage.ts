import { Message, MessageService } from "primeng/api";

export function showMessage(messageService: MessageService, message: Message) {
  messageService.add(message);
}