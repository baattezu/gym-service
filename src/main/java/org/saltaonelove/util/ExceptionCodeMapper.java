package org.saltaonelove.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ExceptionCodeMapper {

    private static final Map<Class<? extends Throwable>, HttpStatus> exceptionMap = new HashMap<>();

    static {
        // Общие ошибки валидации и клиентские ошибки
        exceptionMap.put(IllegalArgumentException.class, HttpStatus.BAD_REQUEST); // Некорректные данные в запросе

        exceptionMap.put(MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST); // Ошибки при проверке тела запроса
        exceptionMap.put(HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST); // Невозможно прочитать тело запроса (например, некорректный JSON)
        exceptionMap.put(MissingServletRequestParameterException.class, HttpStatus.BAD_REQUEST); // Отсутствуют обязательные параметры в запросе

        // Ошибки ресурса
        exceptionMap.put(NoSuchElementException.class, HttpStatus.NOT_FOUND); // Отсутствует элемент в коллекции или базе данных

        // Конфликты и ошибки состояния
        exceptionMap.put(IllegalStateException.class, HttpStatus.CONFLICT); // Ошибка состояния (например, дубликаты)

        // Сетевые ошибки
        exceptionMap.put(HttpServerErrorException.GatewayTimeout.class, HttpStatus.GATEWAY_TIMEOUT); // Таймаут шлюза
        exceptionMap.put(HttpServerErrorException.ServiceUnavailable.class, HttpStatus.SERVICE_UNAVAILABLE); // Сервис недоступен

        // Ошибки сериализации и десериализации
        exceptionMap.put(JsonProcessingException.class, HttpStatus.BAD_REQUEST); // Ошибки обработки JSON
        exceptionMap.put(HttpMessageConversionException.class, HttpStatus.BAD_REQUEST); // Ошибки преобразования данных в запросе/ответе

        // Ошибки сервера
        exceptionMap.put(NullPointerException.class, HttpStatus.INTERNAL_SERVER_ERROR); // Общая ошибка сервера (необработанное исключение)
        exceptionMap.put(RuntimeException.class, HttpStatus.INTERNAL_SERVER_ERROR); // Общая ошибка для необработанных исключений
        exceptionMap.put(Exception.class, HttpStatus.INTERNAL_SERVER_ERROR); // Подстраховка для всех исключений
    }


    /**
     * Возвращает HTTP-код для указанного исключения.
     */
    public static HttpStatus getHttpStatusForException(Throwable exception) {
        return exceptionMap.getOrDefault(exception.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Добавляет новое исключение и его HTTP-код в маппинг.
     */
    public static void addExceptionMapping(Class<? extends Throwable> exceptionClass, HttpStatus httpStatus) {
        exceptionMap.put(exceptionClass, httpStatus);
    }

    /**
     * Удаляет исключение из маппинга.
     */
    public static void removeExceptionMapping(Class<? extends Throwable> exceptionClass) {
        exceptionMap.remove(exceptionClass);
    }

    public static void reset() {
        exceptionMap.clear();
    }


}
