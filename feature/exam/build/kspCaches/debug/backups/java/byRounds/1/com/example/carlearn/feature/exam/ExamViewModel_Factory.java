package com.example.carlearn.feature.exam;

import com.example.carlearn.core.database.dao.QuestionDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class ExamViewModel_Factory implements Factory<ExamViewModel> {
  private final Provider<QuestionDao> questionDaoProvider;

  public ExamViewModel_Factory(Provider<QuestionDao> questionDaoProvider) {
    this.questionDaoProvider = questionDaoProvider;
  }

  @Override
  public ExamViewModel get() {
    return newInstance(questionDaoProvider.get());
  }

  public static ExamViewModel_Factory create(Provider<QuestionDao> questionDaoProvider) {
    return new ExamViewModel_Factory(questionDaoProvider);
  }

  public static ExamViewModel newInstance(QuestionDao questionDao) {
    return new ExamViewModel(questionDao);
  }
}
