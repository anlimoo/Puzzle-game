package tool;

public class Solve {
	private int[][] udlr= {{-1,0,1,0},
			   			   {0,-1,0,1}};

	/*注意，这里UP和DOWN，LEFT和RIGHT必须是两两相对的，因为后面代码中使用
	* ((dPrev != dCurr) && (dPrev%2 == dCurr%2))
	* 来判断前后两个移动方向是否相反
	*/
	private final int UP = 0;
	private final int DOWN = 2;
	private final int LEFT = 1;
	private final int RIGHT = 3;
	
	private int SIZE;
	
	//各个目标点坐标
	private int[][] targetPoints; 
	
	//用于记录移动步骤，存储0,1,2,3,对应上，下，左，右
	private static int[] moves = new int[100000];
	
	private static long ans = 0;; //当前迭代的"设想代价"
	
	//目标状态
	private static int[][] tState;
	
	private static int[][] sState;
	
	private static int blank_row,blank_column;

	@SuppressWarnings("static-access")
	public Solve(int[][] state) {
		SIZE = state.length;
		targetPoints = new int[SIZE * SIZE][2];
		tState=new int[SIZE][SIZE];
		sState=new int[SIZE][SIZE];
		
		for(int i=0;i<SIZE;i++) {
			for(int j=0;j<SIZE;j++) {
				tState[i][j]=i*SIZE+j+1;
				sState[i][j]=state[i][j];
			}
		}
		tState[SIZE-1][SIZE-1]=0;
		
		this.sState = state;
		//得到空格坐标
		for(int i=0;i<state.length;i++) {
			for(int j=0;j<state[i].length;j++) {
				if(state[i][j] == 0) {
					blank_row = i;
					blank_column = j;
					break;
				}
			}
		}

		//得到目标点坐标数组
		for(int i=0;i<state.length;i++) {
			for(int j=0;j<state.length;j++) {
				targetPoints[tState[i][j]][0] = i; //行信息
				targetPoints[tState[i][j]][1] = j; //列信息
			}
		}
	}
	
	public boolean solve(int[][] state,int blank_row,int blank_column,int dep,long d,long h) {
		long h1;
		
		//和目标矩阵比较，看是否相同，如果相同则表示问题已解
		boolean isSolved = true;
		for(int i=0;i<SIZE&&isSolved;i++) {
			for(int j=0;j<SIZE&&isSolved;j++) {
				if(state[i][j] != tState[i][j]) {
					isSolved = false;
				}
			}
		}
		if(isSolved) {
			return true;
		}
		
		if(dep == ans) {
			return false;
		}

		//用于表示"空格"移动后的坐标位置
		
		
		int[] dh1=new int[4];
		long[] dh2=new long[4];
		long[] dh3=new long[4];
		int cou=0;

		for(int direction=0;direction<4;direction++) {
			int blank_row1 = blank_row;
			int blank_column1  = blank_column;
			int[][] state2 = new int[SIZE][SIZE];
			
			//本次移动方向和上次移动方向刚好相反，跳过这种情况的讨论
			if(direction != d && (d%2 == direction%2)) {
				continue;
			}
			
			blank_row1 = blank_row + udlr[0][direction];
			blank_column1 = blank_column + udlr[1][direction];
			
			//边界检查
			if(blank_column1 < 0 || blank_column1 == SIZE || blank_row1 < 0 || blank_row1 == SIZE) {
				continue ;
			}
			
			for(int i=0;i<state.length;i++) {
				for(int j=0;j<state.length;j++) {
					state2[i][j] = state[i][j];
				}
			}
			
			//交换空格位置和当前移动位置对应的单元格	
			state2[blank_row][blank_column] = state2[blank_row1][blank_column1];
			state2[blank_row1][blank_column1] = 0;
			
			//查看当前空格是否正在靠近目标点
			if(direction == DOWN && blank_row1 > targetPoints[state[blank_row1][blank_column1]][0]) {
				h1 = h - 1;
			} else if(direction == UP && blank_row1 < targetPoints[state[blank_row1][blank_column1]][0]){
				h1 = h - 1;
			} else if(direction == RIGHT && blank_column1 > targetPoints[state[blank_row1][blank_column1]][1]) {
				h1 = h - 1;
			} else if(direction == LEFT && blank_column1 < targetPoints[state[blank_row1][blank_column1]][1]) {
				h1 = h - 1;
			} else { 
				//这种情况发生在任意可能的移动方向都会使得估价函数值变大
				h1 = h + 1;
			}
			
			if(h1+dep+1>ans) { //剪枝
				continue;
			}
			
			dh1[cou]=direction;
			dh2[cou]=h1+dep;
			dh3[cou++]=h1;
			for(int i=0;i<cou;i++) {
				if(dh2[cou-1]<dh2[i]) {
					for(int j=cou-1;j>i;j--) {
						dh1[j]=dh1[j-1];
						dh2[j]=dh2[j-1];
						dh3[j]=dh3[j-1];
					}
					dh1[i]=direction;
					dh2[i]=h1+dep;
					dh3[i]=h1;
					break;
				}
			}
			
		}
		
		for(int i=0;i<cou;i++) {
			int blank_row1 = blank_row;
			int blank_column1  = blank_column;
			int[][] state2 = new int[SIZE][SIZE];
			
			blank_row1 = blank_row + udlr[0][dh1[i]];
			blank_column1 = blank_column + udlr[1][dh1[i]];
			
			for(int j=0;j<state.length;j++) {
				for(int k=0;k<state.length;k++) {
					state2[j][k] = state[j][k];
				}
			}
			
			//交换空格位置和当前移动位置对应的单元格	
			state2[blank_row][blank_column] = state2[blank_row1][blank_column1];
			state2[blank_row1][blank_column1] = 0;
			moves[dep] = dh1[i];
			
			//迭代深度求解
			if(solve(state2, blank_row1, blank_column1, dep+1, dh1[i], dh3[i])) {
				return true;
			}
		}
		return false;
	}
	
	
	
	/*得到估价函数值*/
	public int getHeuristic(int[][] state) {
		int heuristic = 0;
		for(int i=0;i<state.length;i++) {
			for(int j=0;j<state[i].length;j++) {
				if(state[i][j] != 0) {
					heuristic = heuristic + 
							Math.abs(targetPoints[state[i][j]][0] - i)
							+ Math.abs(targetPoints[state[i][j]][1] - j);
					
				}
			}
		}
		return heuristic;
	}
	
	public int[] startSlove() {
		int j = getHeuristic(sState);
		for(ans=j;;ans++) {
			if(solve(sState,blank_row,blank_column,0,-1,j)) {
				break;
			}
		}
		
		int[] m=new int[(int)ans];
		for(int i=0;i<ans;i++) {
			m[i]=moves[i];
		}
		return m;
	}
	
}
