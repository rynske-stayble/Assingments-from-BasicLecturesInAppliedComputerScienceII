public class DiningPhilosophers {

  // 哲学者 (箸) の数
  static final int N = 5;
  
  // 箸の配列
  static ChopStick[] chopsticks;

  // 哲学者の配列
  static Philosopher[] philosophers;
  
  public static void main(String[] args) {
    
    // 箸オブジェクトを N 本用意する
    chopsticks = new ChopStick[N];
    
    // オブジェクトの初期化
    for (int i = 0; i < N; i++) {
      chopsticks[i] = new ChopStick();
    }
    
    
    // 哲学者オブジェクトの生成と、
    // 各自が使える箸の登録
    philosophers = new Philosopher[N];
    
    for (int i = 0; i < N; i++) {
      philosophers[i] = new Philosopher(chopsticks[i], 
chopsticks[(i+1)%N]);  
    }
    
    
    // 表示用
    for (int i = 0; i < N; i++) {  
      System.out.printf("Philosopher %d         ", i);
    }
    System.out.println();
    
    for (int i = 0; i < N; i++) {  
      System.out.print("--------------------  ");
    }
    System.out.println();    
    
    // 哲学者達に食事を始めさせる。
    for (int i = 0; i < N; i++) {
      philosophers[i].start();
    }
  }
}

// 箸クラス
class ChopStick {

  static int counter = 0;
  
  // 使用中か否か
  boolean isUsed;

  // 優先度
  int rank;

  ChopStick() {
    isUsed = false;
    
    // それぞれの箸に異なる優先度を与える
    rank = counter++;
  }
}

// 哲学者クラス
class Philosopher extends Thread {
  
  static int counter = 0;
  
  // 待ち時間の最大値。適当に設定してください。
  final long WAITTIME = 100;
  
  // 識別番号。哲学者オブジェクトを複数作るので、
  // 表示の際に見やすいよう、各々に異なるIDを与えます。
  int number;
  
  // 自分のテリトリーにある2本の箸。
  // lower : 優先度の低い箸,
  // higher: 優先度の高い箸
  ChopStick lowerStick;
  ChopStick higherStick;
  
  // 自分が箸を持っているか否か
  boolean hasLowerStick;
  boolean hasHigherStick;
  
  // message
  String msg;

  Philosopher(ChopStick c1, ChopStick c2) {
    // IDの付与
    number = counter++;
    
    // 自分が使える箸を登録
    if (c1.rank < c2.rank) {
      lowerStick = c1;
      higherStick = c2;
    } else {
      lowerStick = c2;
      higherStick = c1;
    }
    
    // 最初は箸を持っていない状態
    hasLowerStick = false;
    hasHigherStick = false;
  }
  
  // 哲学者の行動を登録したメソッド群
  
  public void run () {
    for (int i = 0; i < 5; i++) {
      // 優先度の高い箸を取る
      picUpHigherStick();

      // 考え事をする
      if(hasHigherStick)
        think();
      
      // 優先度の低い箸を取る
      picUpLowerStick();
      
      // 食べる
      eat();
      
      // また考える
      
      think();
      
      // 優先度の高い箸を置く
      putDownHigherStick();
      
      // 優先度の低い箸を置く
      putDownLowerStick();
      
      // またまた考え事をする
      think();
    }
  }
  
  // 優先度の高い箸を取る
  synchronized void picUpHigherStick() {
    
    // 優先度の高い箸が空くまで待つ
    while(higherStick.isUsed)
      await();
    
      higherStick.isUsed = true;
      hasHigherStick = true;
      printAnEvent("pick up stick No." + higherStick.rank);
  }
  
  // 優先度の低い箸を取る
  synchronized void picUpLowerStick() {
    if (hasHigherStick) {

      // 優先度の低い箸が空くまで待つ
      while(lowerStick.isUsed)
        await();
      
      lowerStick.isUsed = true;
      hasLowerStick = true;
      printAnEvent("pick up stick No." + lowerStick.rank);
    }
  }
  
  
  // 食事をするメソッド
  void eat() {
    if (hasLowerStick && hasHigherStick) {
      printAnEvent("***eating***");
      
      // ランダム時間だけ待機
      waitRandom();
    }
  }
  
  // 考え事
  void think() {
    printAnEvent("      ***thinking***");
    
    waitRandom();
  }
  
  // 優先度の高い箸を置く
  synchronized void putDownHigherStick() {
    // この条件は、単に (hasHigherStick) だけでもよい。
    if (hasLowerStick && hasHigherStick) {
      higherStick.isUsed = false;
      hasHigherStick = false;
      
      printAnEvent("put down stick No." + higherStick.rank);
    }
  }
  
  // 優先度の低い箸を置く
  synchronized void putDownLowerStick() {
    if (hasLowerStick) {
      lowerStick.isUsed = false;
      hasLowerStick = false;
      
      printAnEvent("put down stick No." + lowerStick.rank);
    }
  }  
  
  // 表示用
  synchronized void printAnEvent(String str) {

    msg = "";

    for(int i = 0; i < 22 * number; i++) {
      msg += " ";
    }
    msg += str;
    System.out.println(msg);
  }
  
  // 適当な時間だけ待つ
  void waitRandom() {
    try {
      sleep((long)(Math.random() * WAITTIME));
    } catch (InterruptedException e) { }
  }
  
  // 一瞬だけ待つ (これ、別にwaitRandom()メソッドでも代用可)
  synchronized void await() {
    try {
      sleep(1);
    } catch (InterruptedException e) { }    
  }
}

