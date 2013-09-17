package com.diaimm.april.web.taglib.paging;

/**
 * Created with diaimm(봉구).
 * User: diaimm(봉구)
 * Date: 13. 9. 13
 * Time: 오후 9:28
 */
enum PagingPolicy {
	FIXED_SCREEN {
		@Override
		int getScreenFirstIndex(int current, int pageSizePerScreen) {
			return ((current - 1) / pageSizePerScreen) * pageSizePerScreen + 1;
		}

		@Override
		int getScreenLastIndex(int current, int pageSizePerScreen) {
			return (((current - 1) / pageSizePerScreen) + 1) * pageSizePerScreen;
		}

		@Override
		Integer getPrevIndex(int current, int pageSizePerScreen) {
			int screenFirstIndex = this.getScreenFirstIndex(current, pageSizePerScreen);
			return screenFirstIndex - 1 < 1 ? null : screenFirstIndex - 1;
		}

		@Override
		Integer getNextIndex(int current, int pageSizePerScreen, int maxIndex) {
			int screenLastIndex = getScreenLastIndex(current, pageSizePerScreen);
			return screenLastIndex + 1 > maxIndex ? null : screenLastIndex + 1;
		}
	};

	abstract int getScreenFirstIndex(int current, int pageSizePerScreen);

	abstract int getScreenLastIndex(int current, int pageSizePerScreen);

	/**
	 * 이전 screen이 의미가 없다면 null을 리턴합니다.
	 * @param current
	 * @param pageSizePerScreen
	 * @return
	 */
	abstract Integer getPrevIndex(int current, int pageSizePerScreen);

	/**
	 * 다음 screen이 의미가 없다면 null을 리턴합니다.
	 * @param current
	 * @param pageSizePerScreen
	 * @param maxIndex
	 * @return
	 */
	abstract Integer getNextIndex(int current, int pageSizePerScreen, int maxIndex);
}
