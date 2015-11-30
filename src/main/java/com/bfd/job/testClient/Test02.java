package com.bfd.job.testClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author: BFD474
 *
 * @description:
 */
public class Test02 {

	public static int[] server = new int[10];// 机器序号：权重
	public static int cw = 0;
	public static int number = -1;// 当前SERVER的序号,开始是-1
	public static int max;// 最大权重
	public static int gcd;// 最大公约数

	static {
		init();
		max = getMaxWeight(server);
		gcd = gcd(server);
	}

	public static void init() {
		server[0] = 5;
		server[1] = 15;
		server[2] = 10;
		server[3] = 5;
		server[4] = 25;

		server[5] = 5;
		server[6] = 5;
		server[7] = 10;
		server[8] = 11;
		server[9] = 9;

	}

	/**
	 * 求最大公约数
	 * 
	 * @param array
	 * @return
	 */
	public static int gcd(int[] ary) {

		int min = ary[0];

		for (int i = 0; i < ary.length; i++) {
			if (ary[i] < min) {
				min = ary[i];
			}
		}
		while (min >= 1) {
			boolean isCommon = true;
			for (int i = 0; i < ary.length; i++) {
				if (ary[i] % min != 0) {
					isCommon = false;
					break;
				}
			}
			if (isCommon) {
				break;
			}
			min--;
		}
		// System.out.println("gcd=" + min);
		return min;
	}

	/**
	 * 求最大值，权重
	 * 
	 * @return
	 */

	public static int getMaxWeight(int[] ary) {
		int max = 0;
		for (int i = 0; i < ary.length; i++) {
			if (max < ary[i]) {
				max = ary[i];
			}
		}
		return max;
	}

	/**
	 * 获取请求的SERVER序号
	 * 
	 * @return
	 */
	public static Integer next() {

		while (true) {
			number = (number + 1) % server.length;
			// System.out.println("number=" + number);
			// System.out.println("cw=" + cw);
			if (number == 0) {
				cw = cw - gcd;// cw比较因子，从最大权重开始，以最大公约数为步长递减
				if (cw <= 0) {//
					cw = max;
					if (cw == 0)
						return null;
				}
			}
			if (server[number] >= cw)
				return number;
		}

	}

	public static void main(String[] args) {
		try {
			ExecutorService pool = Executors.newFixedThreadPool(1);

			for (int i = 0; i < 100; i++) {
				Runnable run = new Runnable() {
					public void run() {
						System.out.print(Test02.next() + ",");
					}
				};

				pool.execute(run);
			}
			// 关闭启动线程
			pool.shutdown();
			// 等待子线程结束，再继续执行下面的代码
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			System.out.println("all thread complete");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * int req = 99; while (req >= 0) {
		 * System.out.print(RoundRobinWeightTest.next() + ","); req--; }
		 */

	}
}
