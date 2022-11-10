package tatbash.translation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import tatbash.infrastructure.config.ApplicationProperties;
import tatbash.telegram.MessageIn;
import tatbash.telegram.MessageOut;

@Slf4j
@RequiredArgsConstructor
@Service
public class TranslationService {

  private final ApplicationProperties properties;

  public MessageOut translate(MessageIn messageIn) {
    return MessageOut.of(new Update());
  }

  // todo: Сюда приходит update
  //       1. Нужно сделать DTO для Request и Response, and cover by tests
  //          1.1 Нужно идти с конца, от запроса в API Yandex Translate, к наращиванию логики приложения.
  //              Этого принципа нужно придерживаться ВСЕГДА (на работе и на pet-проектах), сначала делаешь
  //              самый минимум, который выполняет то что тебе нужно, покрываешь тестами,
  //              потом наращиваешь вокруг этой функциональности остальную логику слой за слоем, покрывая тестами,
  //              пока не дойдёшь до взаимодействия с пользователем.
  //       2. Проверяем в тексте наличие маркера
  //          2.1 Нужен алгоритм для проверки наличия маркера
  //       3. Если есть маркер #перевод, то выдираем текст для перевода
  //          3.1 Нужен алгоритм для "выдирания" текста, исключая маркеры
  //       4. Выдранный текст отправляем на перевод в translation.yandex
  //          4.1 Чтобы делать запрос в translation.yandex, нужно отправлять
  //              в заголовке Authorization IAM-токен, который "протухает"
  //              каждые 12 часов (рекомендуется обновлять его чаще, например, раз в час)
  //          4.2 Чтобы обновлять IAM-токен, нужен другой токен ("вечный")
  //          4.3 Таким образом, получается, что нужен отдельный поток, который периодически
  //              будет обновлять IAM-токен. Во время разработки, можно, пока, вручную обновлять.
  //              Главное реализовать саму функциональность, а потом остальное докручивать.
}
