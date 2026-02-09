# 🥗 Kalorist - Akıllı Kalori ve Besin Takip Uygulaması

Kalorist, sağlıklı yaşam hedeflerinize ulaşmanıza yardımcı olmak için tasarlanmış, modern ve kullanımı kolay bir Android uygulamasıdır. Günlük kalori alımınızı, makro besin değerlerinizi (Protein, Karbonhidrat, Yağ), su tüketiminizi ve egzersizlerinizi tek bir yerden takip edebilirsiniz.

## 📱 Özellikler

### 🏠 Ana Sayfa (Dashboard)
- **Günlük Özet:** Alınan ve kalan kalorileri, detaylı makro besin çubuklarıyla (Protein, Karbonhidrat, Yağ) görüntüleyin.
- **Öğün Takibi:** Kahvaltı, Öğle, Akşam ve Atıştırmalık öğünlerinize kolayca besin ekleyin.
- **Görsel İlerleme:** Dairesel ilerleme çubukları ve renk kodlu göstergelerle hedeflerinizi anlık izleyin.

### 💧 Su ve Egzersiz Takibi
- **Akıllı Su Takibi:** Günlük su hedefinizi belirleyin ve animasyonlu bardak ikonlarıyla tüketiminizi kaydedin.
- **Egzersiz Kaydı:** Yaptığınız aktiviteleri ve yakılan kalorileri ekleyerek net kalori dengenizi hesaplayın.

### 📊 Detaylı Analiz
- **Gelişmiş Grafikler:** Haftalık ve aylık kalori/makro alımınızı grafiklerle analiz edin.
- **Vücut Analizi:** Kilo, Vücut Kitle İndeksi (VKİ) ve diğer sağlık verilerinizi takip edin.

### 👤 Profil Yönetimi
- **Kişiselleştirilmiş Hedefler:** Yaş, boy, kilo ve aktivite seviyenize göre günlük kalori ihtiyacınızı otomatik hesaplayın.
- **Veri Güvenliği:** Firebase Authentication ile güvenli giriş ve kayıt sistemi.

---

## 🛠️ Teknolojiler ve Mimari

Bu proje, modern Android geliştirme standartlarına uygun olarak **%100 Kotlin** ile geliştirilmiştir.

*   **Mimari:** MVVM (Model-View-ViewModel) + Clean Architecture prensipleri.
*   **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern, deklaratif arayüz tasarımı.
*   **Dependency Injection:** [Hilt](https://dagger.dev/hilt/) - Bağımlılıkların yönetimi.
*   **Veritabanı:** [Room](https://developer.android.com/training/data-storage/room) - Çevrimdışı veri saklama (Offline-first).
*   **Ağ İşlemleri:** [Retrofit](https://square.github.io/retrofit/) & OkHttp.
*   **Backend & Auth:** [Firebase](https://firebase.google.com/) (Authentication, Crashlytics).
*   **Asenkron İşlemler:** Kotlin Coroutines & Flow.
*   **Resim Yükleme:** [Coil](https://coil-kt.github.io/coil/).

---

## 📸 Ekran Görüntüleri

| **Dashboard** | **Besin Detayı** | **Su Takibi** | **Profil** |
|:---:|:---:|:---:|:---:|
| <img src="screenshots/dashboard.png" width="200" /> | <img src="screenshots/food_detail.png" width="200" /> | <img src="screenshots/water_tracker.png" width="200" /> | <img src="screenshots/profile.png" width="200" /> |

*(Not: Ekran görüntüleri klasörüne görsellerinizi ekleyebilirsiniz)*

---

## 🚀 Kurulum (Setup)

Projeyi yerel makinenizde çalıştırmak için aşağıdaki adımları izleyin:

1.  **Projeyi Klonlayın:**
    ```bash
    git clone https://github.com/yusfvural/Kalorist.git
    cd Kalorist
    ```

2.  **Firebase Kurulumu:**
    - Firebase konsolunda yeni bir proje oluşturun.
    - Android uygulamanızı ekleyin (`com.yusufvural.kaloritakip`).
    - İndirdiğiniz `google-services.json` dosyasını projenin `app/` klasörüne yapıştırın.
    *(Not: Bu dosya güvenlik nedeniyle GitHub reposunda bulunmamaktadır.)*

3.  **Projeyi Derleyin:**
    - Android Studio'da projeyi açın.
    - Gradle senkronizasyonunun tamamlanmasını bekleyin.
    - Uygulamayı emülatörde veya fiziksel cihazda çalıştırın.

---

## 🤝 Katkıda Bulunma

Katkılarınızı bekliyoruz! Bir sorun bulursanız veya yeni bir özellik eklemek isterseniz lütfen bir **Issue** açın veya **Pull Request** gönderin.

## 📄 Lisans

Bu proje MIT Lisansı ile lisanslanmıştır. Detaylar için `LICENSE` dosyasına bakabilirsiniz.

---
**Geliştirici:** Yusuf Vural
