package com.bfd.job.testClient;

import java.util.ArrayList;

/**
 * Order负载均衡实现类
 *
 */
public class Test03 {

	private String[] compIdArr = null; // 参加负载均衡的compId数组
	private int[] dispatchCountArr = null; // 负载分配数组

	public Test03() {

	}

	/**
	 * 初始化compId数组和负载分配数组
	 * 
	 * @param scaleStaticList
	 */
	public void init(ArrayList<ScaleStaticsValue> scaleStaticList) {
		if (!scaleStaticList.isEmpty()) {
			compIdArr = new String[scaleStaticList.size()];
			dispatchCountArr = new int[scaleStaticList.size()];
		}

		/**
		 * 静态负载均衡算法的简单说明 实现的问题： 目前有N个资源Scale1~ScaleN，且这N个资源正在处理个数不等的请求，这时发来M个请求。
		 * 如何把M个请求分发到这N个资源中，使得分发完之后这N个资源所处理的请求是均衡的。
		 *
		 * 名词定义 Scale-资源 Order-请求 compId-每个资源的唯一标识
		 *
		 * compId数组-compIdArr 根据每个Scale目前所处理的Order个数多少，从小到大把其对应的compId记录在数组中
		 * 
		 * 负载分配数组-dispatchCountArr 对于dispatchCountArr[i]，它的值表示的是可以分发的Order的个数，
		 * 分发的compId的范围是在compIdArr[0]到compIdArr[i]之间。
		 * 例，如果有3个Scale，它们的compId和当前的Order个数分别为 Scale1：1，Scale2：5，Scale3： 12
		 * 那么根据这组数据可以构造一个负载分配数组 dispatchCountArr[0]=(5-1)*1=4
		 * 表示可以在Scale1上再分配4个Order dispatchCountArr[1]=(12-5)*2=14
		 * 表示可以在Scale1和Scale2上平均分配14个Order dispatchCountArr[2]=整型最大值
		 * 表示可以在Scale1~Scale3上再平均分配任意个Order
		 * 
		 * 当有多个Order订单，需要为每个都分配一个compId时，
		 * 1.先从dispatchCountArr[0]开始，如果dispatchCountArr
		 * [0]不为0，说明可以把这个订单指派给Scale1， 并且dispatchCountArr[0]的值减1；
		 * 
		 * 2.如果发现dispatchCountArr[0]已经为0，则继续看dispatchCountArr[1]，
		 * 如果大于0，说明可以再从Scale1和Scale2中取一个进行指派，用dispatchCountArr[1] mod 2产生一个0到1
		 * 的index，意思是在Scale1和Scale2之间进行平均分配，取compIdArr[index]作为分配的compId，
		 * 同时dispatchCountArr[1]减1
		 * 
		 * 3.如果dispatchCountArr[1]也被减为0，那么继续看dispatchCountArr[2]，类似2中的操作，
		 * 用dispatchCountArr[2] mod 3产生一个0到2的index，意思是在Scale1到Scale3之间进行平均指派，
		 * 取compIdArr[index]作为compId，同时dispatchCountArr[2]减1
		 * 
		 * 4.重复3中的操作，直到所有的Order都已经被分配了一个compId
		 */

		int size = scaleStaticList.size();
		for (int i = 0; i < size; i++) {
			compIdArr[i] = scaleStaticList.get(i).compId;

			if (i == size - 1) {
				// the last
				dispatchCountArr[i] = Integer.MAX_VALUE;
			} else {
				dispatchCountArr[i] = scaleStaticList.get(i + 1).orderCnt
						- scaleStaticList.get(i).orderCnt;
				dispatchCountArr[i] = dispatchCountArr[i] * (i + 1);
			}
		}
	}

	/**
	 * 根据负载均衡算法获取一个分配的compId
	 * 
	 * @return
	 */
	public String getDispatchedcompId() {
		String compId = null;
		if (dispatchCountArr == null) {
			return compId;
		}

		for (int i = 0; i < dispatchCountArr.length; i++) {
			if (dispatchCountArr[i] == 0) {
				// 说明compIdArr[0]~compIdArr[i]已经分配完，需要检查下一个
				continue;
			} else {
				// 否则，在compIdArr[0]~compIdArr[i]的范围内获取一个compId
				int index = 0;
				if (i != 0) {
					index = dispatchCountArr[i] % (i + 1);
				}
				compId = compIdArr[index];
				dispatchCountArr[i]--;
				break;
			}
		}
		return compId;
	}

	public class ScaleStaticsValue {
		public ScaleStaticsValue() {

		}

		public String compId = null; // compId
		public int orderCnt = 0; // order的个数
	}
}
