📸 Mini Instagram 

Java 17+ · Spring Boot 3+ · PostgreSQL · IntelliJ IDEA · Custom Token Authentication (Spring Security kullanılmadan)
Bu proje, verilen gereksinimler doğrultusunda Spring Security KULLANMADAN token tabanlı kimlik doğrulama, kullanıcı yönetimi, rol yönetimi ve içerik yönetimi (post, yorum, beğeni) sağlayan Mini Instagram backend uygulamasıdır.

Tüm API istekleri Postman Collection olarak teslim edilmektedir.


🏁 Kurulum 

Java Version: 21
Spring Boot Version: 3.5.8
Database: PostgreSQL veya Docker üzerinde PostgreSQL


📥 Projeyi GitHub’dan Çekme

git clone https://github.com/acartunahan/mini-instagram.git


▶️ Projeyi Çalıştırma
IntelliJ IDEA Üzerinden

MiniInstagramApplication sınıfını açın
(com.socialmedia.miniinstagram.MiniInstagramApplication)

main metodunun yanındaki Run (▶️) butonuna basın.

# Terminalden çalıştırmak için:
./mvnw spring-boot:run


🗄️ DATABASE Çalıştırma

1️⃣ Docker ile Çalıştırma

docker compose up -d

2️⃣ PostgreSQL Kurulumu

CREATE DATABASE miniinstagram;

YML bağlantı ayarları

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/miniinstagram
    username: miniuser
    password: minipass

👑 Hazır ADMIN Kullanıcısı

Proje ilk ayağa kalktığında otomatik oluşturulur:

username: admin
password: admin123
role: ADMIN

📦 Postman Collection Açıklaması

Projeyle birlikte bir Postman Collection teslim edilir. Bu collection içinde: 

https://.postman.co/workspace/Personal-Workspace~27efebba-f6e3-4596-b961-1387c815ede6/collection/37722490-44911868-f85f-4549-b5b7-a461b8d086b0?action=share&creator=37722490&active-environment=37722490-6f3164aa-63dd-4f34-8c5b-bd306283b06e

Tüm endpoint’ler ilgili klasörler altında gruplanmıştır:

AUTH

USERS

POSTS

COMMENTS

LIKES

Aşağıdaki environment değişkenleri tanımlıdır:

{{baseUrl}} → http://localhost:8080

{{accessToken}} → POST /api/auth/login ile alınan token değeri


TÜM ENDPOINTLER


🔐 AUTH Endpoints

⭐ POST /api/auth/signup

Yeni kullanıcı kaydı (rol otomatik olarak USER).

⭐ POST /api/auth/login

Giriş yapar ve token üretir.

⭐ GET /api/auth/me

Aktif kullanıcının bilgilerini döner.

⭐ POST /api/auth/logout

Mevcut token pasifleştirilir.


👤 USERS Endpoints

⭐ GET /api/users/{id}

Kullanıcının herkese açık profil bilgilerini döner.

⭐ PUT /api/users/me/password

Aktif kullanıcı kendi şifresini günceller (eski şifre doğrulanır).

⭐ DELETE /api/users/me

Aktif kullanıcı hesabını siler.

⭐ DELETE /api/admin/users/{id}

ADMIN başka bir kullanıcıyı silebilir.


🖼️ POSTS Endpoints

⭐ POST /api/posts

Post oluşturma (imageUrl + description).

⭐ GET /api/posts/{id}

Post detayını döner (yorum sayısı, beğeni sayısı, görüntülenme sayısı vs.).

⭐ PUT /api/posts/{id}

Sadece post sahibi veya ADMIN güncelleyebilir.

⭐ DELETE /api/posts/{id}

Sadece post sahibi veya ADMIN silebilir.

⭐ POST /api/posts/{id}/view

Görüntülenme sayısını artırır.

⭐ GET /api/posts

Tüm aktif postları listeler.


💬 COMMENTS Endpoints

⭐ POST /api/posts/{id}/comments

Post’a yorum ekler.

⭐ GET /api/posts/{id}/comments

Post’un yorumlarını listeler.

⭐ DELETE /api/comments/{id}

Silme yetkisi:

Yorum sahibi

Post sahibi

ADMIN


❤️ LIKES Endpoints

⭐ POST /api/posts/{id}/likes

Postu beğenir (kullanıcı başına 1 kez).

⭐ DELETE /api/posts/{id}/likes

Beğeniyi geri alır.


🔁 Örnek API Akış Senaryosu

1.) Kayıt Ol (Signup)
POST /api/auth/signup

Yeni bir kullanıcı oluşturulur.

2.) Giriş Yap (Login)
POST /api/auth/login

Kullanıcı giriş yapar, response içinden token alınır.

Bu token Postman environment’ta {{accessToken}} değişkenine set edilir.

3.) Korumalı Endpointleri Kullan

Örnekler:

POST /api/posts → Post oluştur

GET /api/posts → Postları listele

POST /api/posts/{id}/comments → Yorum ekle

PUT /api/users/me/password → Şifre güncelle

4.) Çıkış Yap (Logout)
POST /api/auth/logout

Mevcut token pasifleştirilir, korumalı endpointlere erişim engellenir.



⚠️ Hata Yönetimi (Global Exception Handling)

Uygulamadaki tüm hatalar tek bir standart JSON formatında döner.
Bunu sağlamak için özel bir global hata yönetimi katmanı uygulandı.

{
    "success": false,
    "message": "Invalid username or password",
    "timestamp": "2025-11-26T17:58:16.7511123"
}

HTTP Status Kodları – Bu Projede Nasıl Kullanılıyor?
🟡 401 Unauthorized

🔴 403 Forbidden

Ne zaman?
Kullanıcı giriş yapmış olsa bile aksiyonu yapmaya yetkisi yoksa.

⚫ 500 Internal Server Error

Ne zaman?
Beklenmeyen, bizim kontrol etmediğimiz bir hata oluşursa (NullPointer, veri tabanı hatası, vs).


Ne zaman?
Kullanıcının kimliği doğrulanmamışsa veya gönderdiği token geçersizse.




🧩 Varsayımlar & Kısıtlar

⭐ Kullanıcı & Kimlik Doğrulama

Token doğrulama tamamen Custom Token Filter üzerinden yapılır.

Token süresi sabittir, refresh token yoktur.

Bir kullanıcı aynı anda birden fazla cihazdan giriş yapabilir.

Şifre sıfırlama (forgot password) özelliği yoktur.

⭐ İçerik Yönetimi

Post resimleri dosya yükleme değildir, imageUrl olarak saklanır.

Post sıralaması zaman bazlıdır, pagination yoktur.

Görüntülenme sayacı manuel artırılır.

⭐ Database & Cascade

Kullanıcı silinince:
Post + Comment + Like + Token hepsi otomatik silinir.

Post silinince:
Yorumlar + Beğeniler silinir.

Kullanıcı adı benzersizdir.

Bir kullanıcı bir postu yalnızca bir kez beğenebilir.



