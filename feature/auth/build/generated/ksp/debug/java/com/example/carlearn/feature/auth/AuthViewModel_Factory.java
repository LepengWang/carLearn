package com.example.carlearn.feature.auth;

import com.example.carlearn.core.database.UserPreferences;
import com.example.carlearn.core.database.dao.UserDao;
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
public final class AuthViewModel_Factory implements Factory<AuthViewModel> {
  private final Provider<UserDao> userDaoProvider;

  private final Provider<UserPreferences> userPreferencesProvider;

  public AuthViewModel_Factory(Provider<UserDao> userDaoProvider,
      Provider<UserPreferences> userPreferencesProvider) {
    this.userDaoProvider = userDaoProvider;
    this.userPreferencesProvider = userPreferencesProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(userDaoProvider.get(), userPreferencesProvider.get());
  }

  public static AuthViewModel_Factory create(Provider<UserDao> userDaoProvider,
      Provider<UserPreferences> userPreferencesProvider) {
    return new AuthViewModel_Factory(userDaoProvider, userPreferencesProvider);
  }

  public static AuthViewModel newInstance(UserDao userDao, UserPreferences userPreferences) {
    return new AuthViewModel(userDao, userPreferences);
  }
}
