package com.example.carlearn.core.database.di;

import com.example.carlearn.core.database.network.QuestionApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class DatabaseModule_ProvideQuestionApiServiceFactory implements Factory<QuestionApiService> {
  @Override
  public QuestionApiService get() {
    return provideQuestionApiService();
  }

  public static DatabaseModule_ProvideQuestionApiServiceFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static QuestionApiService provideQuestionApiService() {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideQuestionApiService());
  }

  private static final class InstanceHolder {
    private static final DatabaseModule_ProvideQuestionApiServiceFactory INSTANCE = new DatabaseModule_ProvideQuestionApiServiceFactory();
  }
}
