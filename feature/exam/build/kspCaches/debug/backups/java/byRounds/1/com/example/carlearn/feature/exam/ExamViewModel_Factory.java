package com.example.carlearn.feature.exam;

import com.example.carlearn.core.database.repository.QuestionRepository;
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
  private final Provider<QuestionRepository> repositoryProvider;

  public ExamViewModel_Factory(Provider<QuestionRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ExamViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static ExamViewModel_Factory create(Provider<QuestionRepository> repositoryProvider) {
    return new ExamViewModel_Factory(repositoryProvider);
  }

  public static ExamViewModel newInstance(QuestionRepository repository) {
    return new ExamViewModel(repository);
  }
}
