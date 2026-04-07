package com.example.carlearn.core.database.repository;

import com.example.carlearn.core.database.dao.QuestionDao;
import com.example.carlearn.core.database.network.QuestionApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class QuestionRepository_Factory implements Factory<QuestionRepository> {
  private final Provider<QuestionDao> questionDaoProvider;

  private final Provider<QuestionApiService> questionApiServiceProvider;

  public QuestionRepository_Factory(Provider<QuestionDao> questionDaoProvider,
      Provider<QuestionApiService> questionApiServiceProvider) {
    this.questionDaoProvider = questionDaoProvider;
    this.questionApiServiceProvider = questionApiServiceProvider;
  }

  @Override
  public QuestionRepository get() {
    return newInstance(questionDaoProvider.get(), questionApiServiceProvider.get());
  }

  public static QuestionRepository_Factory create(Provider<QuestionDao> questionDaoProvider,
      Provider<QuestionApiService> questionApiServiceProvider) {
    return new QuestionRepository_Factory(questionDaoProvider, questionApiServiceProvider);
  }

  public static QuestionRepository newInstance(QuestionDao questionDao,
      QuestionApiService questionApiService) {
    return new QuestionRepository(questionDao, questionApiService);
  }
}
