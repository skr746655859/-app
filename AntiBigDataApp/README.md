# 商品数据存储与上传

## 代码概览

商品数据的存储是放在本地的，并且能够标记数据的状态（是否已上传，上传成功与否），以便后续可以再次处理。

存储和上传的代码详细细节都可以参见MainActivity，以下只将纲要列出。

- Dao对象：ProductsDao，提供针对商品数据存储的基础的增删查改操作
- Worker对象：UploadProductWorker，通过WorkManager创建Work Request，将数据上传到服务器。创建Request时，传入Product在本地数据库的ID用以通知Worker要上传哪一个数据。可以通过getWorkInfoByIdLiveData对上传进度进行监测。

## 存储

### 依赖注入导入（推荐）

- Hilt参考文档：https://developer.android.google.cn/training/dependency-injection/hilt-android?hl=zh_cn

- 实例

  1. 对被注入的类进行注解

     ```java
     @AndroidEntryPoint
     public class MyService extends Service {
     
     	// ....
         
     }
     ```

  2. 注入Dao对象

     ```java
     @Inject 
     private ProductsDao productsDao;
     ```

  3. 在代码中引用**productsDao**即可对本地商品数据进行增删查改

     ```java
     // 参照Product类的备注根据情况填写参数
     productsDao.insertAll(new Product(.....)); 
     ```

### 代码生成对象

```java
ProductsDatabase productsDatabase = Room
        .databaseBuilder(this, ProductsDatabase.class, "products")
        .build();	// 获得Database
ProductsDao productsDao = productsDatabase.productsDao(); // 获得Dao对象
productsDao.insertAll(new Product(....));
```

## 上传

- Worker参考文档

- 示例：

  1. 生成参数，告知Worker要上传哪个Product

     ```java
     Data data = new Data.Builder()
                  .putLong(Key.ID, productId)   // Product的ID
                  .build();
     ```

  2. 生成Request

     ```java
     // OneTimeWorkRequest 为一次性的Request
     // 并且在加入Work队列后，会立马执行
     OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(UploadProductWorker.class)
                  .setInputData(data)
                  .build();
     ```

  3. 获取WorkManager实例，并将Request加入队列

     ```java
     WorkManager workManager = WorkManager.getInstance(context);
          workManager.enqueue(request);
     ```

  4. 观察上传任务的状态

     ```java
     workManager.getWorkInfoByIdLiveData(request.getId()).observe(this, new Observer<WorkInfo>() {
      	@Override
      	public void onChanged(WorkInfo workInfo) {
           // 在此处观察任务状态
           // 并将任务处理结果保存到数据库
           // 数据处理细节请见MainActivity
      	}
            
     });   
     ```