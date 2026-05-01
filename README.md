# 🍽️ Kalorist - Kalori Takip Uygulaması

Kalorist, günlük kalori alımınızı kolayca takip etmenizi sağlayan modern bir Android uygulamasıdır. Material Design 3 ve Jetpack Compose ile geliştirilmiştir.

[![Android](https://img.shields.io/badge/Android-35+-green)](https://www.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-purple)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Latest-blue)](https://developer.android.com/jetpack/compose)
[![Material3](https://img.shields.io/badge/Material3-Design-orange)](https://m3.material.io)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

## ✨ Özellikler

- 📊 **Günlük Kalori Takibi** - Yiyeceklerinizin kalorilerini takip edin
- 🎯 **Hedef Belirle** - Günlük kalori hedefini ayarlayın
- 📈 **İstatistikler** - Kalori alımınızı görselleştirin
- 🎨 **Modern UI** - Material Design 3 ile güzel arayüz
- 🌙 **Tema Desteği** - Açık ve koyu tema
- ⚡ **Hızlı ve Responsive** - Smooth bir deneyim

## 🛠️ Teknoloji Yığını

| Teknoloji | Versiyon |
|-----------|----------|
| Kotlin | Latest |
| Android | 24+ (API Level) |
| Jetpack Compose | Latest |
| Material 3 | Latest |
| Lifecycle | Latest |
| ViewModel | Latest |
| Navigation Compose | Latest |

## 🚀 Başlangıç

### Ön Koşullar

- **Android Studio**: Ladybug (2024.2.1) veya daha yeni
- **JDK**: 11 veya daha yeni
- **Android SDK**: Minimum API 24

### Kurulum

1. **Repository'yi klonlayın**
   ```bash
   git clone https://github.com/yusfvural/kalorist.git
   cd kalorist
   ```

2. **Android Studio'da açın**
   - Android Studio'yu açın
   - "Open" seçin ve proje klasörünü seçin
   - Gradle senkronizasyonunu bekleyin

3. **Uygulamayı çalıştırın**
   ```bash
   ./gradlew build
   ./gradlew installDebug
   ```
   
   Veya Android Studio'da "Run" tuşuna basın (Shift + F10)

## 📱 Kullanım

### Ana Ekran
1. Bugün tükettiğiniz kalorilerini görün
2. Hedef kalorisine kalan günü takip edin
3. Yeni bir yiyecek eklemek için "+" butonuna basın

### Yiyecek Ekleme
1. Yiyecek adını girin
2. Kalorisi belirtin
3. Ekle butonuna basın

### İstatistikler
- Haftalık kalori grafiği
- Günlük ortalama tüketim
- Hedef karşılaştırması

## 🏗️ Proje Yapısı

```
kalorist/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/yusufvural/kaloritakip/
│   │   │   │   ├── ui/          # UI bileşenleri
│   │   │   │   ├── viewmodel/   # ViewModel'lar
│   │   │   │   ├── data/        # Data layer
│   │   │   │   └── MainActivity.kt
│   │   │   └── res/
│   │   └── test/
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## 🔨 Geliştirme

### Kod Standartları

- **Kotlin Code Style**: [Official Kotlin Style Guide](https://kotlinlang.org/docs/coding-conventions.html)
- **Naming**: camelCase için değişkenler, PascalCase için sınıflar
- **Comments**: Kompleks logic için Türkçe veya İngilizce yorum
- **Formatting**: 4 space indentation

### Commit Mesajları

Aşağıdaki formatı kullanın:

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Types:**
- `feat`: Yeni özellik
- `fix`: Hata düzeltmesi
- `docs`: Dokümantasyon
- `style`: Kod formatı (mantık değişmez)
- `refactor`: Kod yeniden yapılandırması
- `test`: Test ekleme/düzeltme
- `chore`: Build, dependency vb.

**Örnek:**
```
feat(ui): Kalori grafiği eklendi

Kullanıcıların haftalık kalori alımını görebilmesi için
bar chart bileşeni eklendi.

Closes #123
```

## 🤝 Katkı Yapma

Katkılarınız bize değerli! Lütfen [CONTRIBUTING.md](CONTRIBUTING.md) dosyasını okuyun.

### Hızlı Başlangıç

1. **Fork edin** bu repository'yi
2. **Feature branch oluşturun** (`git checkout -b feature/AmazingFeature`)
3. **Değişikliklerinizi commit edin** (`git commit -m 'feat: Add AmazingFeature'`)
4. **Branch'inize push edin** (`git push origin feature/AmazingFeature`)
5. **Pull Request açın**

## 🐛 Hata Bildirme

Hata bulduğunuz takdirde [Issue](https://github.com/yusfvural/kalorist/issues) açabilirsiniz.

Lütfen aşağıdaki bilgileri ekleyin:
- Hata açıklaması
- Yeniden üretme adımları
- Beklenen davranış
- Ekran görüntüsü (varsa)
- Cihaz bilgisi (Model, Android sürümü)

## 📝 Lisans

Bu proje [MIT License](LICENSE) altında lisanslanmıştır. Detaylar için [LICENSE](LICENSE) dosyasına bakın.

## 👨‍💻 Geliştirici

- **Yusuf Vural** - [@yusfvural](https://github.com/yusfvural)

## 🙏 Teşekkürler

- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern UI Toolkit
- [Material Design 3](https://m3.material.io) - Design System
- [Android Architecture Components](https://developer.android.com/topic/architecture) - MVVM Pattern

## 📞 İletişim

- GitHub Issues: [Bir issue açın](https://github.com/yusfvural/kalorist/issues)
- Email: yusfvurl51@gmail.com

## 🗺️ Roadmap

- [ ] Veritabanı entegrasyonu (Room)
- [ ] Yiyecek veritabanı
- [ ] Makro/Mikro besin takibi
- [ ] Egzersiz takibi
- [ ] Cloud senkronizasyonu
- [ ] Kullanıcı profili
- [ ] Bildirimler

---

**Yıldız atarsan çok seviniriz!** ⭐

Sorularınız veya önerileriniz için bize ulaşın!
