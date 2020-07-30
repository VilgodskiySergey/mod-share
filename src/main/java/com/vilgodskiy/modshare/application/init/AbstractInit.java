package com.vilgodskiy.modshare.application.init;

import com.vilgodskiy.modshare.init.domain.InitScript;
import com.vilgodskiy.modshare.init.domain.InitScriptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;

/**
 * @author Vilgodskiy Sergey 24.07.2020
 * <p>
 * Abstract init script - provide wrapper and base methods for Init-Script
 */
public abstract class AbstractInit implements SmartInitializingSingleton {

    @Autowired
    private InitScriptRepository initScriptRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;

    private final String className = getClass().getSimpleName();

    @Override
    public void afterSingletonsInstantiated() {
        if (initScriptRepository.findByName(getClass().getSimpleName()).isEmpty()) {
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    init();
                    initScriptRepository.save(new InitScript(className, LocalDateTime.now()));
                }
            });
        }
    }

    protected abstract void init();
}