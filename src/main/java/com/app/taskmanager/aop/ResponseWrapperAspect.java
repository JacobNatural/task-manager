package com.app.taskmanager.aop;

import com.app.taskmanager.dto.response.ResponseDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Aspect responsible for automatically wrapping REST controller responses
 * into a {@link com.app.taskmanager.dto.response.ResponseDto} object.
 * <p>
 * It applies to all classes annotated with {@code @RestController}.
 * If a controller method returns a {@link reactor.core.publisher.Mono}, the result
 * will be transformed into a {@code Mono<ResponseDto>}. If the controller already
 * returns a {@code ResponseDto}, the response remains unchanged.
 * <p>
 * If the {@link Mono} is empty, it returns {@code new ResponseDto<>(null)}.
 *
 * <p><b>Example:</b></p>
 * <pre>
 * {@code
 * @GetMapping("/tasks")
 * public Mono<List<TaskResponseDto>> getTasks() {
 *     return taskService.getAllTasks();
 * }
 *
 * // The response will automatically be wrapped as:
 * // Mono<ResponseDto<List<TaskResponseDto>>>
 * }
 * </pre>
 *
 * @see com.app.taskmanager.dto.response.ResponseDto
 */
@Component
@Aspect
public class ResponseWrapperAspect {

    /**
     * Intercepts all methods in classes annotated with {@code @RestController}
     * and wraps their responses in a {@link ResponseDto} if the return type is {@link Mono}.
     *
     * @param joinPoint the join point representing the intercepted controller method invocation
     * @return the original object returned by the controller method,
     *         or a {@code Mono<ResponseDto>} if the return type is {@link Mono}
     * @throws Throwable if the controller method throws any exception
     */
    @Around("within(@org.springframework.web.bind.annotation.RestController *) && !within(org.springdoc..*) ")
    public Object wrapControllerResponse(ProceedingJoinPoint joinPoint) throws Throwable {

        var returned = joinPoint.proceed();

        if (!(returned instanceof Mono<?> mono)) {
            return returned;
        }

        return mono.map(body -> {
            if (body instanceof ResponseDto){
                return body;
            }
            return new ResponseDto(body);
        }).defaultIfEmpty(new ResponseDto<>(null));
    }
}

