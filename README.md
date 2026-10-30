---

# 4rchLogin 🛡️

A **4rchLogin** egy modern, kifejezetten proxy-alapú Minecraft hálózatokhoz (BungeeCord / Waterfall) tervezett, nagy teljesítményű és pehelykönnyű autentikációs plugin, amely a maximális biztonságra, a gyors működésre és a minimalista dizájnra törekszik.

---

## 🚀 Főbb jellemzők

* **Proxy-szintű architektúra:** A hitelesítés közvetlenül a hálózati rétegben történik, így a backend szerverek teljes biztonságban vannak az illetéktelen vagy be nem jelentkezett játékosoktól.
* **Zero-Bloat / Teljesítményre optimalizálva:** Nincsenek felesleges, erőforrás-zabáló háttérfolyamatok; tiszta Java kóddal és Gradle build rendszerrel lett felépítve a zökkenőmentes futás érdekében.
* **Biztonságos jelszókezelés:** Modern és biztonságos hash-elési eljárások védi a regisztrált fiókok adatait.
* **Intelligens sessionkezelés:** Megjegyzi a hitelesített játékosokat egy ideig, így nem kell minden egyes csatlakozásnál újra beírni a jelszót, ha azonos IP-cínről vagy sessionből lépnek be.
* **Zökkenőmentes átirányítás:** Sikeres bejelentkezés vagy regisztráció után azonnal a megfelelő lobbiba vagy játéktérre dobja a felhasználót.

---

## 📋 Parancsok

* `/login <jelszó>` – Bejelentkezés a már létező fiókba.
* `/register <jelszó> <jelszó_újra>` – Új fiók regisztrálása és biztonságos zárolása.

---

## ⚙️ Konfiguráció és Testreszabás

A plugin egy áttekinthető, könnyen szerkeszthető YAML konfigurációs fájllal dolgozik, ahol testre szabhatod:

* A chatre kiírt üzeneteket, előtagokat és figyelmeztetéseket.
* A session időtartamát (mennyi ideig maradjon bejelentkezve a játékos kilépés után).
* A kötelezően elvégzendő parancsokat és a be nem jelentkezett játékosokra vonatkozó korlátozásokat (mozgás-, chat- és parancstilalom a hitelesítés előtt).

---
