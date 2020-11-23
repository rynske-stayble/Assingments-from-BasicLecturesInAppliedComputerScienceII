// 共有オブジェクト
class Fork {
	// どの哲学者の左手にあるか識別する番号
	private int id;
	// いずれかの哲学者の手にとられているかどうか
	private boolean eating = false;
	
	// コンストラクタ
	Fork(int i) {
		// メソッド引数の哲学者の左手に置かれている
		id = i;
	}
	
	// 手にとられる
	public synchronized void pick(int i) {
		while (eating == true) {
		// 隣の哲学者の手に取られている間は繰り返し
			try {
				System.out.println(i + " is starving.");
				// 待機プールへ
				wait();
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
		// 手に取れたら true をセット
		eating = true;
	}
	
	// 手から離される
	public synchronized void down() {
		// 食事が済んだら false をセット
		eating = false;
		// 待機プールのスレッドをロック探索状態に
		notifyAll();
	}
}

class Philosopher implements Runnable {
	int id;       	// 哲学者の識別番号
	int eatTime;	// 食事時間
	int thinkTime;	// 思索時間
	int left;     	// 左手のフォークの番号
	int right;    	// 右手のフォークの番号
	Fork[] forks = new Fork[4];	// 共有オブジェクト
	
	// コンストラクタ
	Philosopher(int i) {
		id = i;
	}
	
	// 哲学者のプロパティのセット
	public void setProperties(int eating, int thinking, Fork[] objs) {
		left = id;
		if (id != 0) { 
			right = id - 1;
		} else {
			right = 4;
		}
		eatTime = eating;
		thinkTime = thinking;
		forks = objs;
	}
	
	// 空腹を感じるとフォークを手に取る
	public void feelHungry() {
		// 左手のフォークを手に取る
		forks[left].pick(id);
		
		// ここの待機時間が長いとデッドロック発生
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			System.out.println(e);
		}
		
		// 右手のフォークを手に取る
		forks[right].pick(id);
		
		System.out.println(id + " is eating.");
		try {
			// 食事中
			Thread.sleep(eatTime);
		} catch (InterruptedException e) {
			System.out.println(e);
		}
		// 食事終了
		// 左手のフォークを離す
		forks[left].down();
		// 右手のフォークを離す
		forks[right].down();
	}
	
	// 思索
	public void think() {
		try {
			// 思索中
			Thread.sleep(thinkTime);
		} catch (InterruptedException e) {
			System.out.println(e);
		}
	}
	
	// スレッドの run() メソッド
	public void run() {
		while (true) {
			// 思索中
			System.out.println(id + " is thinking.");
			think();
			// 空腹を感じる
			System.out.println(id + " feels hungry.");
			feelHungry();
		}
	}
}

class Dining {
	public static void main(String[] args) {
		// 共有オブジェクト
		Fork[] forks = new Fork[5];
		// 哲学者
		Philosopher[] phils = new Philosopher[5];
		
		// インスタンス化
		for (int i=0; i<5; i++) {
			forks[i] = new Fork(i);
			phils[i] = new Philosopher(i);
		}
		
		// 哲学者のプロパティ
		//                     eating, thinking, shared object
		phils[0].setProperties(2000,   1000,     forks);
		phils[1].setProperties(1900,   1100,     forks);
		phils[2].setProperties(1800,   1200,     forks);
		phils[3].setProperties(1700,   1300,     forks);
		phils[4].setProperties(1600,   1400,     forks);
		
		// スレッド
		Thread[] thres = new Thread[5];
		for (int i=0; i<5; i++) {
			// 哲学者をスレッドに委譲
			thres[i] = new Thread(phils[i]);
		}
		
		// スレッドの開始
		for (int i=0; i<5; i++) {
			thres[i].start();
		}
	}
}

