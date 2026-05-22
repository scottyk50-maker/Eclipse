@ECHO OFF
ECHO com.dstsystems.bps.conversion.cipher.AESCipher %0 %1 %2 %3
java -cp ./AESCipher.jar;./3rdPartyLibs/Jodd/jodd-3.5.2.jar com.dstsystems.bps.cipher.AESCipher %1 %2
