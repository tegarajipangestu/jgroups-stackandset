
# jgroups-stackandset

Implementasi JGroups untuk Replicated Stack and Replicated Set

By:

Fauzan Hilmi Ramadhian - 13512003
Tegar Aji Pangestu - 13612061

## How to run

 1. ```clone``` repository ini
 2. ```cd``` ke ```jgroups-stackandset/bin```
 3. Jalankan program dengan mengetik ```java -jar jgroups-stackandset.jar```

## How to test
### Replicated Stack

 1. Ketika terminal menunjukkan tulisan ```Type "stack" to stack and type "set" to set```, pilih Stack dengan mengetik ```stack```
 2. Untuk melakukan ```push``` ke Stack, cukup ketik ```push <somestring>```
 3. Untuk melakukan ```pop``` dari Stack, cukup ketik ```pop```
 4. Untuk melihat ```top``` dari Stack, cukup ketik ```top```
 5. Ketika kamu merasa kamu sudah cukup, ketik ```quit``` untuk keluar dari program

### Test Case

#### Mengecek elemen teratas stack yang telah terisi oleh client lain

 1. Jalankan 2 client
 2. Pada client 1, masukkan perintah ```push halo```
 4. Pada client 2 masukkan perintah ```top```
 5. Pada client 2 akan muncul teks ```halo```

#### Mengecek elemen teratas stack kosong

 1. Jalankan 1 client
 2. Masukkan perintah ```top```
 3. Akan muncul peringatan ```Stack empty```
 
#### Komunikasi 3 client
 
1.  Jalankan 2 client
2. Pada client 1, masukkan perintah ```push halo sayang``` 
3. Pada client 2, masukkan perintah ```top```
4. Pada client 2 akan muncul ```halo sayang```
5. Jalankan client ketiga
6. Pada client 3, masukkan perintah ```push aku orang ketiga```
7. Pada client 2, masukkan perintah ```pop```
8. Pada client 2 akan muncul ```Popped String : aku orang ketiga```
9. Pada client 1, masukkan perintah ```top```
10. Pada client 1 akan muncul ```halo sayang```
11. Pada client 2, masukkan perintah ```pop```
12. Pada client 2 akan muncul ```Popped String : halo sayang```
13. Pada client 1, masukkan perintah ```top```
14. Pada client 1 akan muncul ```Stack empty``` 

### Replicated Set

 1. Ketika terminal menunjukkan tulisan ```Type "stack" to stack and type "set" to set```, pilih Set dengan mengetik ```set```
 2. Untuk melakukan ```add``` ke Set, cukup ketik ```add <somestring>```
 3. Untuk melakukan ```remove``` dari Set, cukup ketik ```remove <somestring>```
 4. Untuk mengecek apakah sebuah Set ```contains``` sebuah string, cukup ketik ```contains <somestring>```
 5. Ketika kamu merasa kamu sudah cukup, ketik ```quit``` untuk keluar dari program

### Test Case

#### Mengecek apakah sebuah string terdapat di set

 1. Jalankan 2 client
 2. Pada client 1, masukkan perintah ```add halo```
 3. Pada client 2 masukkan perintah ```contains halo```
 4. Pada client 2 akan muncul teks ```halo belongs to set```

#### Menghapus member set

 1. Jalankan 2 client
 2. Pada client 1, masukkan perintah ```add halo```
 3. Pada client 2 masukkan perintah ```remove halo```
 4. Pada client 1 masukkan perintah ```contains halo```
 5. Pada client 2 akan muncul teks ```halo not belongs to set```
 
#### Komunikasi 3 client
 
1.  Jalankan 2 client
2. Pada client 1, masukkan perintah ```add halo sayang``` 
3. Pada client 2, masukkan perintah ```contains halo sayang```
4. Pada client 2 akan muncul ```halo sayang belongs to set```
5. Jalankan client ketiga
6. Pada client 3, masukkan perintah ```add aku orang ketiga```
7. Pada client 2, masukkan perintah ```contains aku orang ketiga```
8. Pada client 2 akan muncul ```aku orang ketiga belongs to set```
9. Pada client 1, masukkan perintah ```remove aku orang ketiga```
10. Pada client 1 masukkan perintah ```contains aku orang ketiga``` 
11. Pada client 1, akan muncul ```aku orang ketiga not belongs to set```

> Written with [StackEdit](https://stackedit.io/).
