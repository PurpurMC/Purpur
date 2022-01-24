var Bukkit = Java.type('org.bukkit.Bukkit');
 
// 该函数是插件执行的入口，当插件被启用时调用
function main() {
  log('Core Run OK! Stop server to do next job');
  Bukkit.shutdown();
}
 
 
var name = 'AutoStop';
var version = '1.0.0';
var author = 'FlyProject';
 
// PVPINCore 对此API的实现在 src/main/javascript/api.js
