# Mantenha os membros das classes kotlin
-keep class kotlin.** { *; }

# Mantenha as classes de entidades de banco de dados Room
-keep class com.hilguener.gymtracker.data.db.entity.** { *; }

# Mantenha as classes dos DAOs Room
-keep class com.hilguener.gymtracker.data.db.dao.** { *; }

# Mantenha as classes dos repositórios
-keep class com.hilguener.gymtracker.data.repository.** { *; }

# Mantenha as classes de ViewModel
-keep class com.hilguener.gymtracker.ui.viewmodel.** { *; }

# Mantenha todas as classes de atividades
-keep class com.hilguener.gymtracker.ui.activity.** { *; }

# Mantenha as interfaces Parcelable
-keep interface android.os.Parcelable

# Mantenha as implementações do Creator
-keep class * implements android.os.Parcelable$Creator {
    *;
}

# Mantenha classes de modelos usadas para análise JSON
-keep class com.hilguener.gymtracker.model.** { *; }

# Mantenha as classes de utilitários
-keep class com.hilguener.gymtracker.util.** { *; }

# Mantenha as classes de constantes
-keep class com.hilguener.gymtracker.constant.** { *; }

# Mantenha as classes de custom views
-keep class com.gymtracker.customview.** { *; }

# Mantenha classes para manipulação de eventos de clicar
-keep class com.gymtracker.clickhandler.** { *; }

# Mantenha a classe de inicialização do aplicativo
-keep class com.hilguener.gymtracker.MainApplication{ *; }

# Mantenha as bibliotecas de terceiros
-keep class com.squareup.picasso.** { *; }
-keep class com.squareup.okhttp.** { *; }
-keep class com.google.android.material.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class androidx.navigation.** { *; }
-keep class androidx.paging.** { *; }

# Mantenha as anotações personalizadas
-keep @interface com.hilguener.gymtracker.customannotation.**




