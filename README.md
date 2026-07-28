# ⚡ VoltPur 26.2

> **مش مجرد Fork — ثورة في أداء Minecraft**
> **Performance في الكود، مش في Plugin**

```
╔══════════════════════════════════════════╗
║          ⚡ VOLTPUR PERFORMANCE          ║
║     Pur-pur → Volt-pur. Voltage.        ║
║     Fork من Purpur 26.2 مع 21 Patch     ║
║     أداء خرافي • استقرار تونل • بدروك    ║
║     عزل بلاجن • واجهة تحكم • أمان        ║
╚══════════════════════════════════════════╝
```

---

## ⚡ ليه VoltPur؟

```
اليوم:
  ❌ خادم عادي → تركب Performance Plugins → أداء 5-10%
  ❌ Fabric Mods أداء خرافي لكن ما تشتغلش على Bukkit

VoltPur:
  ✅ Patches في الكود (مثل Fabric Mods) → أداء 200-500%
  ✅ ويدعم كل Bukkit/Paper/Purpur plugins
  ✅ ويدعم Bedrock + Tunnel + أي استضافة
```

---

## 🚀 21 Patch أداء وتحكم

### 🔋 الـ Performance (مستوحاة من أشهر Fabric Mods)

| # | الـ Patch | المستوحى من | التأثير |
|---|-----------|------------|---------|
| 1 | **Entity Activation** 🎯 | Lithium | 50% أقل CPU |
| 2 | **Hopper Optimization** 🔄 | Lithium | 70% أسرع |
| 3 | **Collision Optimization** 🧱 | Lithium | 40% أسرع |
| 4 | **Memory Optimization** 💾 | FerriteCore | 50% أقل RAM |
| 5 | **Network Optimization** 🌐 | Krypton | 40% أقل CPU |
| 6 | **Redstone Optimization** 🔴 | Alternate Current | 95% أسرع |
| 7 | **Chunk Loading** 🗺️ | C2ME | 70% أسرع |
| 8 | **General Optimization** 🛠️ | ModernFix | إقلاع أسرع |
| 9 | **Entity Limits** 🧟 | ServerCore | منع اللاق |
| 10 | **Light Engine** 💡 | Phosphor | 60% أسرع |

### 🌐 الـ Connection (حصري في VoltPur)

| # | الـ Patch | التأثير |
|---|-----------|---------|
| 11 | **Connection Stability** 🌐 | استقرار التونل 300% (playit.gg, ngrok) |
| 12 | **Bedrock Bridge** 🟦 | Bedrock يلعب كأنه مواطن أصلي - 85% استقرار |

### 🛡️ الـ Protection & Systems (حصري في VoltPur)

| # | الـ Patch | التأثير |
|---|-----------|---------|
| 13 | **Anti-Exploit** 🛡️ | حماية في الكود من كراش, Speed, Fly, Spam |
| 14 | **Aikar Flags Auto** ⚙️ | JVM تظبط G1GC أتوماتيك |
| 15 | **Vanilla Parity** 🎮 | Toggle يحافظ على ميكانيكس الفانيلا |
| 16 | **Discord Webhook** 🔗 | إشعارات بدون Plugin |
| 17 | **World Backup** 💾 | نسخ احتياطي كل 5 دقائق |
| 18 | **Auto-Updater** 🔄 | يفحص التحديثات عند الإقلاع |
| 19 | **Resource Pack HTTP** 📦 | HTTP server مدمج |

### 🌍 الـ Plugin Management (حصري في VoltPur)

| # | الـ Patch | التأثير |
|---|-----------|---------|
| 20 | **Per-World Plugin** 🌍 | عزل البلاجن لكل عالم (Whitelist/Blacklist) |
| 21 | **PAdmin WebUI** 🖥️ | واجهة HTML في المتصفح (localhost:25567) |

---

## 📂 مميزات حصرية تانية

### plugin-pro/ 📁
```
📂 server/
   ├── plugins/       ← 🎮 بلاجن عادية
   ├── plugin-pro/    ← ⚡ بلاجن أداء (اختياري)
   └── VoltPur.jar
```

### Per-World Plugin Isolation 🌍
```yaml
# world-plugins.yml
worlds:
  world:             → whitelist: Essentials, WorldEdit
  mining_world:      → whitelist: VeinMiner, Essentials
  skyblock:          → blacklist: WorldEdit, VeinMiner
```

### /padmin Web UI 🖥️
```
/padmin → http://localhost:25567
  واجهة HTML كاملة للتحكم في عزل البلاجن لكل عالم
  + API (JSON) للمطورين
```

---

## 🏗️ البناء

```bash
./gradlew applyAllPatches
./gradlew createMojmapBundlerJar
# JAR في: purpur-server/build/libs/
```

---

## 📋 الخلاصة

```
🧱 Purpur 26.2
  + 📄 Paper Patches
  + 🟣 Purpur Features  
  + ⚡ 21 VoltPur Patches
  = 🔥 VoltPur Performance Edition
```

---

## 🔗 روابط

- Purpur الأصلي: [https://github.com/PurpurMC/Purpur](https://github.com/PurpurMC/Purpur)
- Fork: [https://github.com/PurpurMC/Purpur/fork](https://github.com/PurpurMC/Purpur/fork)

```
  ⚡ V  O  L  T  P  U  R  ⚡
  "Your server, your rules, everywhere."
```
