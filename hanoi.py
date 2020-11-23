def hanoi(n, a, b, c):
    if n > 0:
	hanoi(n-1, a, c, b)
	print ("move %s: %s --> %s" %(n, a, c))
	hanoi(n-1, b, a, c)
hanoi(4, "A", "B", "C")

