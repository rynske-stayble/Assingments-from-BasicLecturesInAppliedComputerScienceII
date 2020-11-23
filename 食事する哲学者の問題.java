/**
 * デッドロックが発生する場合
 */
public class 食事する哲学者の問題 {
    static final int NUM = 5; // 哲学者の数 (=フォークの数)
 
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // ログの形式を「[<時:分:秒>] <ロガー名>番目の<呼び出し元> <メッセージ>」にしとく
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tT] %3$s番目の%2$s %5$s%n");
 
        List<フォーク> fs = Stream.generate(フォーク::new).limit(NUM).collect(Collectors.toList());
 
        new ForkJoinPool(NUM).submit(() -> {
            IntStream.range(0, NUM).parallel().forEach(i -> {
                フォーク 左 = fs.get(i);
                フォーク 右 = fs.get((i + 1) % NUM);
                Logger logger = Logger.getLogger(String.valueOf(i));
                try {
                    new 哲学者(左, 右, logger).食事();
                } catch (InterruptedException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            });
        }).get();
    }
}
 
class フォーク {
    ReentrantLock lock = new ReentrantLock();
 
    void 使用する() {
        lock.lock();
    }
 
    void 解放する() {
        lock.unlock();
    }
}
 
class 哲学者 {
    static final int NUM_TURNS = 10; // 食事中のスパゲティを食べる回数
    static Random rand = new Random(System.currentTimeMillis());
    Logger logger;
    フォーク 左のフォーク;
    フォーク 右のフォーク;
 
    哲学者(フォーク 左, フォーク 右, Logger logger) {
        this.左のフォーク = 左;
        this.右のフォーク = 右;
        this.logger = logger;
    }
 
    void 食事() throws InterruptedException {
        logger.info("開始");
        for (int i = 0; i < NUM_TURNS; i++) {
            logger.log(Level.INFO, "{0}ターン目", i);
            左のフォークをとりあげる();
            右のフォークをとりあげる();
            スパゲティをボウルからよそって食べる();
            右のフォークを置く();
            左のフォークを置く();
        }
        logger.info("終了");
    }
 
    void 左のフォークをとりあげる() {
        long start = System.currentTimeMillis();
        左のフォーク.使用する();
        long end = System.currentTimeMillis();
        logger.log(Level.INFO, "思索した時間は{0}秒でした", ((end - start) / 1000.0));
    }
 
    void 右のフォークをとりあげる() {
        long start = System.currentTimeMillis();
        右のフォーク.使用する();
        long end = System.currentTimeMillis();
        logger.log(Level.INFO, "思索した時間は{0}秒でした", ((end - start) / 1000.0));
    }
 
    void スパゲティをボウルからよそって食べる() throws InterruptedException {
        logger.info("");
        Thread.sleep(1000 + rand.nextInt(1000)); // 適当な時間経過
    }
 
    void 左のフォークを置く() {
        左のフォーク.解放する();
        logger.info("");
    }
 
    void 右のフォークを置く() {
        右のフォーク.解放する();
        logger.info("");
    }
}

