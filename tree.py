class Tree(object):
	def __init__(self):
		self.tree = []
	def add(self, d):
		self.tree = self.tree + [d]
	def remove(self, d):
		self.tree.remove(d)
	def print(self, end='\n'):
		print("(", end=' ')
		for x in self.tree:
			if isinstance(x, Tree):
				x.print(end=' ')
			else:
				print(x, end=' ')
		print(") ", end=end)

