plugins {
    id("com.android.application")
    id("com.google.gms.google-services")


}



android {
    namespace = "com.example.cathelp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cathelp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


    }
    dataBinding{
        enable = true
    }
    buildFeatures{
        viewBinding=true
    }

    buildTypes {
        release {
            isDebuggable=true

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


    dependencies {


        implementation ("androidx.core:core-splashscreen:1.2.0-alpha01")
        implementation("com.saadahmedev.popup-dialog:popup-dialog:2.0.0")
        implementation("com.github.chivorns:smartmaterialspinner:2.0.0")

        implementation ("com.squareup.retrofit2:retrofit:2.11.0")
        implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
        implementation ("com.squareup.retrofit2:adapter-rxjava3:2.11.0")

        implementation ("io.reactivex.rxjava3:rxandroid:3.0.2")
        implementation ("io.reactivex.rxjava3:rxjava:3.1.8")


        implementation ("com.github.bumptech.glide:glide:4.16.0")
        implementation ("androidx.room:room-runtime:2.6.1")
        implementation("androidx.core:core:1.13.1")
        annotationProcessor ("androidx.room:room-compiler:2.6.1")
        implementation ("androidx.room:room-rxjava3:2.6.1")

        implementation("de.hdodenhof:circleimageview:3.1.0")
        implementation("io.github.pilgr:paperdb:2.7.2")
        implementation("androidx.cardview:cardview:1.0.0")
        implementation("androidx.recyclerview:recyclerview:1.3.2")
        implementation("com.github.rey5137:material:1.3.1")


        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("com.google.android.material:material:1.11.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("com.google.firebase:firebase-database:20.3.1")
        implementation ("com.firebaseui:firebase-ui-database:8.0.2")
        implementation("com.google.firebase:firebase-storage:20.3.0")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
        implementation("androidx.navigation:navigation-fragment:2.7.7")
        implementation("androidx.navigation:navigation-ui:2.7.7")
        implementation("androidx.activity:activity:1.9.0")
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

        implementation("com.google.android.gms:play-services-maps:18.2.0")

        implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
        implementation("com.google.firebase:firebase-analytics:21.6.2")

        implementation ("com.squareup.okhttp3:okhttp:4.12.0")
        implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")



        api ("com.theartofdev.edmodo:android-image-cropper:2.8.0")

        implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
        implementation("com.google.firebase:firebase-auth:22.3.1")
        implementation ("androidx.fragment:fragment:1.6.2")



        implementation ("com.google.android.gms:play-services-maps:18.2.0")
        implementation ("com.google.android.material:material:1.11.0")
        implementation ("com.google.android.libraries.places:places:3.4.0")
        implementation ("io.nlopez.smartlocation:library:3.3.3")
        implementation ("com.github.suchoX:PlacePicker:1.1.2")

        implementation ("com.squareup.picasso:picasso:2.8")






        implementation ("com.github.Mindinventory:VanillaPlacePicker:0.3.1")






    }
