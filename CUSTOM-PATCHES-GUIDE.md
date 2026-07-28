# 🛠️ دليل إضافة Patches - VoltPur 26.2

## ⚡ VoltPur Patch System

### النظام الهرمي للـ Fork:

```
Minecraft Vanilla (Mojang)
    ↓
Paper Patches (تحسينات Paper)
    ↓
Purpur Patches (ميزات Purpur)
    ↓
🌟 VoltPur Patches (تحسيناتك الأداء) 🌟
```

كل patch عبارة عن **تعديل في الكود المصدري** لمinecraft بيتم تطبيقه قبل compile.
ده الفرق بينا وبين الـ plugins:
- **Plugin**: بيعمل API calls بعد ما السيرفر شغال → بطيء ومحدود
- **Patch**: بيعدل في الكود نفسه قبل ما يتcompile → سريع جدًا وفعال

---

## 🎯 خطوات إضافة Patch جديد

### الطريقة الأولى (للمبتدئين) - التعديل المباشر:

```bash
# 1. طبق كل الـ patches
./gradlew applyAllPatches

# 2. افتح الكود في أي IDE (IntelliJ, VS Code)
# الكود هتلاقيه في:
#   purpur-server/build/paper-mojmap-structure/
#   أو بعد التطبيق في:
#   paper-server/
#   purpur-server/src/

# 3. عدل في الكود اللي عايزه

# 4. ارجع اعمل rebuild للـ patches
./gradlew rebuildPatches

# 5. هتلاقي patch جديد في مجلد patches
ls purpur-server/patches/features/
```

### الطريقة الثانية (للمحترفين) - الكتابة المباشرة:

```patch
From: اسمك
Date: التاريخ
Subject: [PATCH] اسم التحسين

diff --git a/src/main/java/net/minecraft/.../FileName.java b/src/main/java/net/minecraft/.../FileName.java
index abc123..def456 100644
--- a/src/main/java/net/minecraft/.../FileName.java
+++ b/src/main/java/net/minecraft/.../FileName.java
@@ -رقم_السطر,عدد_الأسطر +رقم_السطر_الجديد,عدد_الأسطر_الجديد @@
+        // VoltPur start - اسم التحسين
+        الكود_الجديد
+        // VoltPur end
}
```

---

## 💡 أفكار تحسينات أداء قوية

### 1️⃣ Entity Activation - التفعيل الذكي للكائنات
**الكود المستهدف:** `net/minecraft/server/level/ServerChunkCache.java`
- الكائنات البعيدة جدًا (أكثر من 128 بلوك): tick كل 20 tick
- الكائنات المتوسطة (32-128 بلوك): tick كل 5 tick
- الكائنات القريبة: tick طبيعي

### 2️⃣ Hopper Optimization - تحسين الهوبر
**الكود المستهدف:** `net/minecraft/world/level/block/entity/HopperBlockEntity.java`
- الهوبر الفاضي ينام 20 tick قبل ما يعيد check
- تقليل حسابات الـ pull/push غير الضرورية

### 3️⃣ Redstone Optimization - تحسين الريدستون
**الكود المستهدف:** `net/minecraft/world/level/block/RedStoneWireBlock.java`
- تقليل تحديثات الريدستون غير الضرورية
- دمج التحديثات المتكررة

### 4️⃣ Chunk Loading - تحسين تحميل الكانكس
**الكود المستهدف:** `net/minecraft/server/level/ChunkMap.java`
- تخزين مؤقت للـ chunks القريبة
- تحسين خوارزمية unload

### 5️⃣ Collision Optimization - تحسين التصادم
**الكود المستهدف:** `net/minecraft/world/entity/Entity.java`
- تقليل حسابات التصادم للكائنات البعيدة
- تحسين الـ AABB checks

### 6️⃣ Mob AI Ticking - تحسين AI
**الكود المستهدف:** `net/minecraft/world/entity/Mob.java`
- الكائنات البعيدة: AI أبطأ
- الكائنات القريبة فقط: AI كامل

### 7️⃣ Light Engine - تحسين الإضاءة
**الكود المستهدف:** `net/minecraft/world/level/light/LevelLightEngine.java`
- تحسين خوارزميات الإضاءة
- تقليل حسابات الإضاءة غير الضرورية

### 8️⃣ TNT Optimization - تحسين أداء الديناميت
**الكود المستهدف:** `net/minecraft/world/entity/item/PrimedTnt.java`
- تحديد عدد أقصى للـ TNT المنفجر في نفس الوقت
- تحسين حسابات الانفجارات

---

## 📊 مقارنة: Patch vs Plugin

| الميزة | Performance Plugin | Performance Patch (VoltPur) |
|--------|-------------------|-------------------------------|
| **السرعة** | ⭐⭐ | ⭐⭐⭐⭐⭐ |
| **التحكم في الكود** | API فقط | أي حاجة في الكود |
| **تأثير على الـ TPS** | 5-10% | 30-200%+ |
| **استهلاك CPU** | عالي | منخفض |
| **التوافق** | مشاكل مع plugins تانية | 100% توافق |
| **صعوبة التطوير** | سهلة | متوسطة-صعبة |

---

## 🔍 إزاي تعرف إيه الكود اللي محتاج تتعدل؟

1. **شوف الـ Spark/ Timings reports** - هتظهرلك إيه أكتر حاجة بتستهلك CPU
2. **ابحث في كود Minecraft** عن الحاجات دي
3. **شوف Paper patches** إزاي عاملين التحسينات عشان تتعلم
4. **جرب وأضيف patch** واختبر الفرق

```bash
# مثال: عايز تعرف إيه اللي بيستهلك CPU كتير
# شوف الـ timings report في السيرفر
# أو استخدم Spark plugin عشان تحلل
```

---

## ✅ خطواتك الجاية

- [ ] ⭐ **Fork** المستودع على GitHub
- [ ] 🚀 clone المستودع على جهازك
- [ ] 🔧 ركب Java 21+ 
- [ ] 📝 ابدأ بإضافة patches الأداء
- [ ] 🏗️ ابن السيرفر واختبره
- [ ] 📊 قارن الأداء مع Purpur العادي

---

---

## 📂 نظام المجلدين: `plugins/` + `plugin-pro/`

### ✨ الفكرة

بدل ما كل الـ plugins في مجلد واحد، فصلناهم لمجلدين عشان تحكم أكبر:

```
server/
├── plugins/          ← 🎮 Plugins عادية (WorldEdit, MiniGames, Protection)
│   ├── WorldEdit.jar
│   ├── GriefPrevention.jar
│   └── MiniGames.jar
│
├── plugin-pro/       ← ⚡ Performance Plugins (بتتحمل بشكل منفصل)
│   ├── Spark.jar
│   ├── ClearLag.jar
│   └── KeepChunks.jar
│
└── server.jar
```

### 🎯 ليه المجلدين أفضل؟

| الميزة | مجلد واحد (plugins/) | مجلدين (plugins/ + plugin-pro/) |
|--------|---------------------|-------------------------------|
| **ترتيب التحميل** | عشوائي | Performance first 🥇 |
| **التحكم** | صعب تعرف مين أداء ومين عادي | **فصل تام** 🔒 |
| **التحديث** | تخلط كل حاجة | كل مجلد ليه استخدامه |
| **العزل** | Performance plugin لو علقت تأثر على كل حاجة | الأداء معزول ✅ |
| **الإدارة** | تفتش على الملفات عشان تلاقي | واضح ومباشر |

### 🛠️ إزاي تضبطه؟

Patch إضافة `plugin-pro/` مجهز بالفعل في:
```
purpur-server/paper-patches/files/src/main/java/io/papermc/paper/plugin/PluginInitializerManager.java.patch
```

### 📝 إزاي تستخدمه؟

```bash
# مجلد الـ Performance plugins
mkdir plugin-pro
# حط فيه Spark, ClearLag, وغيرها
cp Spark.jar plugin-pro/
cp ClearLag.jar plugin-pro/

# ومجلد الـ plugins العادي
cp WorldEdit.jar plugins/
cp GriefPrevention.jar plugins/
```

الـ Performance plugins في `plugin-pro/` تتحمل **بنفس نظام Paper plugins** لكن بمجلد منفصل.

---

## 🟦 Bedrock Connection Bridge - Patch #0012

### 🧠 الفكرة

هذا الـ Patch يحول VoltPur لسيرفر **يدرك Bedrock players** — يكشفهم تلقائياً ويطبق إعدادات QoS مختلفة لهم.

### 🎯 المشكلة اللي يحلها:

| المشكلة | قبل | بعد |
|---------|-----|-----|
| Bedrock ping عالي | كيك بسبب invalid movement | **Tolerance ×2.5, Auto-recovery** |
| UDP packet loss | قطع اتصال فوري | **إعادة إرسال + استعادة موقع** |
| Login timeout | 30 ثانية (غير كافي) | **60 ثانية للـ Bedrock** |
| Geyser client brand | غير مقروء | **يكشفها ويصنف اللاعب** |

### 🛠️ الملفات المستهدفة:

| الملف | التعديل |
|------|---------|
| `ServerCommonPacketListenerImpl.java` | نظام الكشف + QoS التكيفي |
| `ServerGamePacketListenerImpl.java` | تحسين حركة Bedrock |
| `ServerLoginPacketListenerImpl.java` | وقت تسجيل أطول |
| `CraftPlayer.java` | `isBedrockPlayer()` API |

### 🔍 إزاي يكتشف Bedrock؟

```
1️⃣ Floodgate API (الأدق)
   → org.geysermc.floodgate.api.FloodgateAPI.isBedrockPlayer(uuid)

2️⃣ Client Brand (Geyser يرسله)
   → "Geyser" / "bedrock" في اسم العميل

3️⃣ اسم المستخدم
   → يبدأ بنقطة (بادئة Floodgate)

4️⃣ تحليل الـ Ping
   → Ping > 300ms مستمر = Tunnel أو Bedrock
```

### 📊 نظام QoS التكيفي:

```
اكتشاف Bedrock
    ↓
يطبق: ConnectionProfile.BEDROCK
    ├── KeepAlive: 1 ثانية
    ├── Timeout: 60 ثانية
    ├── Movement Tolerance: x2.5
    └── Login Timeout: 60 ثانية

اكتشاف Tunnel (Java)
    ↓
يطبق: ConnectionProfile.JAVA_TUNNEL
    ├── KeepAlive: 2 ثانية
    ├── Timeout: 45 ثانية
    ├── Movement Tolerance: x1.8
    └── Login Timeout: 45 ثانية

اكتشاف Direct (Java)
    ↓
يطبق: ConnectionProfile.JAVA_DIRECT
    ├── KeepAlive: 1 ثانية
    ├── Timeout: 30 ثانية
    ├── Movement Tolerance: x1.0
    └── Login Timeout: 30 ثانية
```

### 📝 كيفية الاستخدام:

```java
// في أي Plugin:
Player player = Bukkit.getPlayer("name");

// هل هو Bedrock؟
if (player instanceof CraftPlayer cp && cp.isBedrockPlayer()) {
    player.sendMessage("§b🟦 Welcome Bedrock Player!");
}

// حالة الاتصال
if (player instanceof CraftPlayer cp) {
    int stability = cp.getConnectionStabilityScore();
    int latency = cp.getLatency();
}
```

> 💡 **ملاحظة**: الـ Patch يشتغل تلقائياً بدون أي إعدادات. فقط حط Geyser-Spigot.jar و Floodgate.jar في plugins/ واشتغل!
