package anna3;

public class Ant2 {
	public int x;
	public int y;
	SingleCluster cluster;

	public Ant2(SingleCluster cluster) {
		setCluster(cluster);
	}

	public void setCluster(SingleCluster cluster) {
		this.cluster = cluster;
		x = cluster.L / 2 + 1;
		y = x;
	}

	public void step() {			
		int nx = x;
		int ny = y;
		do 
		{	nx= x;
			ny= y;
 			int var = (int) (Math.random() * 4);
			switch (var) {
			case 0:
				nx++;
				break;
			case 1:
				ny++;
				break;
			case 2:
				ny--;
				break;
			case 3:
				nx--;
				break;
			}

		}while (cluster.site[nx][ny] !=1);

		x = nx;
		y = ny;

	}

	public int getR() {
		int dx = x - cluster.L / 2 + 1;
		int dy = y - cluster.L / 2 + 1;
		return (dx * dx + dy * dy);
	}

}
