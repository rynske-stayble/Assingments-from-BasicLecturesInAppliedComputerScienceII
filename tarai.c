#include <stdio.h>
#include <stdlib.h>
#include <time.h>

static long tarai(long, long, long);
int
main(int argc, char **argv)
{
	clock_t start, end;
	start = clock();
	printf("%ld\n", tarai(13, 5, 0));
	end = clock();
	printf("Tarai関数(13,5,0)の実行時間: %f秒\n", (double)(end-start) / CLOCKS_PER_SEC);

	exit(0);
}

static long
tarai(long x, long y, long z)
{
	if (x<=y) {
		return y;
	}
	else {
		return tarai(tarai(x-1, y, z),
				tarai(y-1, z, x),
				 tarai(z-1, x, y));
	}
}

