package com.yangyang.redis;

import com.yangyang.redis.pojo.dto.User;
import com.yangyang.redis.utils.RedisLock;
import net.bytebuddy.asm.Advice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisSpringbootApplicationTests {
    public static  String cacheKey="SenseTime:SenseFace:Test:%s";
    @Autowired
    private StringRedisTemplate stringre;

    @Autowired
    private RedisTemplate<String,Serializable> re;

    @Test
    public void ll(){
//        stringre.opsForList().leftPush("xiaohua","lanlan");
//        stringre.opsForList().leftPush("xiaohua","xiaohau");
        String xiaohua = stringre.opsForList().leftPop("xiaohua");
        System.out.println(xiaohua);
    }

    @Test
    public void ioio(){
        stringre.opsForValue().set("xiaohua","huahua");
        String xiaohua = stringre.opsForValue().get("xiaohua");
        System.out.println(xiaohua);
    }
    @Test
    public void mm(){
        stringre.opsForList().rightPush("xiaoming","hehe");
        stringre.opsForList().rightPush("xiaoming","lianlian");
    }


    @Test
    public void contextLoads() {
        String s = UUID.randomUUID().toString();
        String format = String.format(cacheKey, s);
        stringre.opsForValue().set(format,"i am handsome");
    }

    @Test
    public void hh(){
        String s = stringre.opsForValue().get("SenseTime:SenseFace:Test:87c5a72e-060b-424d-90b2-e9b56044bd5e");
        System.out.println(s);
    }

    @Test
    public void kk(){
        Long k = stringre.opsForValue().increment("ll",1);
        System.out.println(k);
    }

    @Test
    public void aa(){
        String s = UUID.randomUUID().toString();
        String format = String.format(cacheKey, s);
        re.opsForValue().set(format, new User(2L, "u1","pa"));
    }

    @Test
    public void jj(){
        Serializable serializable = re.opsForValue().get("SenseTime:SenseFace:Test:7a5570dc-46ef-4dc6-9226-82b85e6404c0");
        System.out.println(serializable);
    }

    @Test
    public void bb(){
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        IntStream.range(0, 10).forEach(i->{
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    Long guo = stringre.opsForValue().increment("guo", i);
                    System.out.println(guo);
                }
            });
        });
    }

    @Test
    public void ff() throws IllegalAccessException, InstantiationException {
        User serializable =(User) re.opsForValue().get("SenseTime:SenseFace:Test:a4565edc-5970-446a-8d55-2c463b55f66f");
        System.out.println(serializable.getId());
        System.out.println(serializable.getUsername());
        System.out.println(serializable.getPassword());

    }

    @Test
    public void testPipeLine(){
        re.opsForValue().set("a",1);
        re.opsForValue().set("b",2);
        re.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                for (int i = 0;i < 10;i++){
                    redisConnection.incr("a".getBytes());
                }
                System.out.println("a:"+re.opsForValue().get("a"));
                re.opsForValue().set("c",3);
                for(int j = 0;j < 20;j++){
                    redisConnection.incr("b".getBytes());
                }
                System.out.println("b:"+re.opsForValue().get("b"));
                System.out.println("c:"+re.opsForValue().get("c"));
                redisConnection.closePipeline();
                return null;
            }
        });
        System.out.println("b:"+re.opsForValue().get("b"));
        System.out.println("a:"+re.opsForValue().get("a"));
    }

    @Test
    public void zz(){
        stringre.setEnableTransactionSupport(true);
        stringre.opsForValue().set("bb","1231");
        String bb = stringre.opsForValue().get("bb");
        System.out.println(bb);
    }
    @Test
    public void ss(){
        Long stringValueLength = stringre.opsForValue().size("stringValue");
        System.out.println("通过size(K key)方法获取字符串的长度:"+stringValueLength);
        double stringValueLong = stringre.opsForValue().increment("longValue",6);
        System.out.println("通过increment(K key, long delta)方法以增量方式存储long值:" + stringValueLong);
    }

    @Test
    public void nn(){
        boolean absentBoolean = stringre.opsForValue().setIfAbsent("absentValue","fff");
        System.out.println("通过setIfAbsent(K key, V value)方法判断变量值absentValue是否存在:" + absentBoolean);
        if(absentBoolean){
            String absentValue = stringre.opsForValue().get("absentValue")+"";
            System.out.println(",不存在，则新增后的值是:"+absentValue);
            boolean existBoolean = stringre.opsForValue().setIfAbsent("absentValue","eee");
            System.out.print(",再次调用setIfAbsent(K key, V value)判断absentValue是否存在并重新赋值:" + existBoolean);
            if(!existBoolean){
                absentValue = stringre.opsForValue().get("absentValue")+"";
                System.out.println("如果存在,则重新赋值后的absentValue变量的值是:" + absentValue);
            }
        }
    }

    @Test
    public void qq() throws InterruptedException {
        stringre.opsForValue().set("guozhiyang","xiaohei",5,TimeUnit.SECONDS);
        String guozhiyang = stringre.opsForValue().get("guozhiyang");
        System.out.println(guozhiyang);
        Thread.sleep(6*1000);
        String guozhiyang1 = stringre.opsForValue().get("guozhiyang");
        System.out.println(guozhiyang1);
    }

    @Test
    public void ww(){
        Map valueMap = new HashMap();
        valueMap.put("valueMap1","map1");
        valueMap.put("valueMap2","map2");
        valueMap.put("valueMap3","map3");
        re.opsForValue().multiSet(valueMap);
    }
    @Test
    public void uu(){
        //根据List集合取出对应的value值
        List paraList = new ArrayList();
        paraList.add("valueMap1");
        paraList.add("valueMap2");
        paraList.add("valueMap3");
        List<String> valueList = re.opsForValue().multiGet(paraList);
        for (String value : valueList){
            System.out.println("通过multiGet(Collection<K> keys)方法获取map值:" + value);
        }
    }

    @Test
    public void rr(){
        re.opsForList().leftPush("list","a");
        re.opsForList().leftPush("list","b");
        re.opsForList().leftPush("list","c");
    }

    @Test
    public void qw(){
        String listValue = re.opsForList().index("list",0) + "";
        System.out.println("通过index(K key, long index)方法获取指定位置的值:" + listValue);
    }

    @Test
    public void hg(){
        List<Serializable> list =  re.opsForList().range("list",0,-1);
        System.out.println("通过range(K key, long start, long end)方法获取指定范围的集合值:"+list);
    }

    @Test
    public void abc(){
        re.opsForList().leftPush("list","a","n");
        List<Serializable> list =  re.opsForList().range("list",0,-1);
        System.out.println("通过leftPush(K key, V pivot, V value)方法把值放到指定参数值前面:" + list);
    }
    
    @Test
    public void bcd(){
        re.opsForList().leftPushAll("list","w","x","y");
        List<Serializable> list =  re.opsForList().range("list",0,-1);
        System.out.println("通过leftPushAll(K key, V... values)方法批量添加元素:" + list);
    }

    @Test
    public void gah(){
        re.opsForList().rightPush("list","w");
        List<Serializable> list =  re.opsForList().range("list",0,-1);
        System.out.println("通过rightPush(K key, V value)方法向最右边添加元素:" + list);
    }
    @Test
    public void lk(){
        re.opsForList().rightPush("list","w","r");
        List<Serializable> list =  re.opsForList().range("list",0,-1);
        System.out.println("通过rightPush(K key, V pivot, V value)方法向最右边添加元素:" + list);
    }

    @Test
    public void gg(){
        long listLength = re.opsForList().size("list");
        System.out.println("通过size(K key)方法获取集合list的长度为:" + listLength);
    }

    @Test
    public void ds(){
        Serializable presentList = re.opsForList().leftPop("list", 3, TimeUnit.SECONDS);
        System.out.print("通过leftPop(K key, long timeout, TimeUnit unit)方法移除的元素是:" + presentList);
        List<Serializable> list = re.opsForList().range("list", 0, -1);
        System.out.println(",剩余的元素是:" + list);
    }

    @Test
    public void ii(){
        Object popValue = re.opsForList().leftPop("list");
        System.out.print("通过leftPop(K key)方法移除的元素是:" + popValue);
        List<Serializable> list =  re.opsForList().range("list",0,-1);
        System.out.println(",剩余的元素是:" + list);
    }
    @Test
    public void ui(){
        re.opsForHash().put("hashValue","map3","map1-1");
        re.opsForHash().put("hashValue","map4","map2-2");
    }

    @Test
    public void kj(){
        List<Object> hashList = re.opsForHash().values("hashValue");
        System.out.println("通过values(H key)方法获取变量中的hashMap值:" + hashList);
    }

    @Test
    public void ad(){
        Map<Object,Object> map = re.opsForHash().entries("hashValue");
        System.out.println("通过entries(H key)方法获取变量中的键值对:" + map);
    }

    @Test
    public void az(){
        Object mapValue = re.opsForHash().get("hashValue","map3");
        System.out.println("通过get(H key, Object hashKey)方法获取map键的值:" + mapValue);
    }

    @Test
    public void qwe(){
        double hashIncDouble = re.opsForHash().increment("hashInc","map1",3);
        System.out.println("通过increment(H key, HK hashKey, double delta)方法使变量中的键以值的大小进行自增长:" + hashIncDouble);
    }

    @Test
    public void dfg(){
        re.opsForHash().putIfAbsent("hashValue1","map6","map6-6");
        Map<Object, Object> map = re.opsForHash().entries("hashValue1");
        System.out.println("通过putIfAbsent(H key, HK hashKey, HV value)方法添加不存在于变量中的键值对:" + map);
    }

    @Test
    public void re(){
        re.opsForHash().delete("hashValue","map3","map4");
        Map<Object, Object> map = re.opsForHash().entries("hashValue");
        System.out.println("通过delete(H key, Object... hashKeys)方法删除变量中的键值对后剩余的:" + map);
    }

    @Test
    public void abcd(){
        re.opsForSet().add("storeSetValue","F");
    }

    @Test
    public void lkj(){
        Object popValue = re.opsForSet().pop("setValue");
        System.out.print("通过pop(K key)方法弹出变量中的元素:" + popValue);
        Set<Serializable> set = re.opsForSet().members("setValue");
        System.out.println(",剩余元素:" + set);
    }

    @Test
    public void fds(){
        boolean isMove = re.opsForSet().move("setValue","F","destSetValue");
        if(isMove){
            Set<Serializable>set = re.opsForSet().members("setValue");
            System.out.print("通过move(K key, V value, K destKey)方法转移变量的元素值到目的变量后的剩余元素:" + set);
            set = re.opsForSet().members("destSetValue");
            System.out.println(",目的变量中的元素值:" + set);
        }
    }

    @Test
    public void poi(){
        List list = new ArrayList();
        list.add("destSetValue");
        Set differenceSet = re.opsForSet().difference("setValue",list);
        System.out.println("通过difference(K key, Collection<K> otherKeys)方法获取变量中与给定集合中变量不一样的值:" + differenceSet);
    }

    @Test
    public void yu(){
        Set differenceSet = re.opsForSet().difference("setValue","destSetValue");
        System.out.println("通过difference(K key, Collection<K> otherKeys)方法获取变量中与给定变量不一样的值:" + differenceSet);
    }

    @Test
    public void jhg(){
        re.opsForSet().differenceAndStore("setValue","destSetValue","storeSetValue");
        Set set = re.opsForSet().members("storeSetValue");
        System.out.println("通过differenceAndStore(K key, K otherKey, K destKey)方法将求出来的差值元素保存:" + set);
    }

    @Test
    public void gfdsa(){
        Set set = re.opsForSet().distinctRandomMembers("setValue",2);
        System.out.println("通过distinctRandomMembers(K key, long count)方法获取去重的随机元素:" + set);
    }

    @Test
    public void lklj(){
        List<String> list = new ArrayList<>();
        list.add("storeSetValue");
        list.add("destSetValue");
        Set set = re.opsForSet().intersect("setValue",list);
        System.out.println("通过intersect(K key, K otherKey)方法获取交集元素:" + set);
    }

    @Test
    public void ok(){
        re.opsForZSet().add("zSetValue","A",1);
        re.opsForZSet().add("zSetValue","B",3);
        re.opsForZSet().add("zSetValue","C",2);
        re.opsForZSet().add("zSetValue","D",5);
    }

    @Test
    public void qweqw(){
        Set zSetValue = re.opsForZSet().range("zSetValue",0,-1);
        System.out.println("通过range(K key, long start, long end)方法获取指定区间的元素:" + zSetValue);
    }

    @Test
    public void ada(){
        RedisZSetCommands.Range range = new RedisZSetCommands.Range();
        range.lt("D");
        Set<Serializable> zSetValue = re.opsForZSet().rangeByLex("zSetValue", range);
        System.out.println("通过rangeByLex(K key, RedisZSetCommands.Range range)方法获取满足非score的排序取值元素:" + zSetValue);
    }


    @Test
    public void msda(){
        BoundHashOperations<String, String, Object> boundHashOperations = re.boundHashOps("li");
        boundHashOperations.put("ww","i");
        boundHashOperations.put("w1","i1");
        boundHashOperations.put("w2","i2");
    }

    @Test
    public void ashdjask(){
        BoundValueOperations boundValueOperations = stringre.boundValueOps("dasd");
        boundValueOperations.append("a");
        boundValueOperations.append("b");
        System.out.println("获取从指定位置开始，到指定位置为止的值:" + boundValueOperations.get(0,2));
        System.out.println("获取所有值:" + boundValueOperations.get());
        boundValueOperations.set("f");
        System.out.println("重新设置值:" + boundValueOperations.get());
    }

    @Test
    public void asdasd(){
        BoundHashOperations<String, String, Object> boundHashOperations = re.boundHashOps("li");
//        boundHashOperations.put("ww","i");
//        boundHashOperations.put("w1","i1");
//        boundHashOperations.put("w2","i2");
        System.out.println("获取设置的绑定key值:" + boundHashOperations.getKey());
        //获取map中的value值
        boundHashOperations.values().forEach(v -> System.out.println("获取map中的value值" + v));
        //获取map键值对
        boundHashOperations.entries().forEach((m,n) -> System.out.println("获取map键值对:" + m + "-" + n));
        //获取map键的值
        System.out.println("获取map建的值:" + boundHashOperations.get("w1"));

        boundHashOperations.keys().forEach(v -> System.out.println("获取map的键:" + v));
    }
    static int i=50;
    @Autowired
    private RedisLock redisLock;
    @Test
    public void iil(){
//        for(int i=0;i<1;i++){
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    testguo();
//                }
//            };
//            Thread thread = new Thread(runnable);
//            thread.start();
//        }
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                testguo();
            }
        });
    }
    public void testguo(){
        long l = System.currentTimeMillis() + 10;
        boolean xiaohua = redisLock.lock("lili", String.valueOf(l));
        if(xiaohua){
            System.out.println(Thread.currentThread().getName()+"获得了锁");
            System.out.println(--i);
            redisLock.unlock("lili",String.valueOf(l));
        }
    }
}
