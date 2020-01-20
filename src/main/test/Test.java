import org.apache.ibatis.executor.resultset.ResultSetHandler;
public class Test {
    public static void main(String[] args) {
//        Object obj = new Object();
//        ReferenceQueue<Object> refQueue = new ReferenceQueue<Object>();
//        WeakReference<Object> weakRef = new WeakReference<Object>(obj, refQueue);
//        System.out.println(weakRef.get()); // java.lang.Object@f9f9d8
//        System.out.println(refQueue.poll());// null
//
//        // 清除强引用,触发GC
//        obj = null;
//        System.gc();
//
//        System.out.println(weakRef.get());
//
//        // 这里特别注意:poll是非阻塞的,remove是阻塞的.
//        // JVM将弱引用放入引用队列需要一定的时间,所以这里先睡眠一会儿
//        // System.out.println(refQueue.poll());// 这里有可能是null
//
//        Thread.sleep(200);
//        System.out.println(refQueue.poll());
    }
}
