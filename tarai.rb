#tarai.rb
#
#たらい回し関数のPureRuby実装版

require 'benchmark'

puts Benchmark::CAPTION

def tarai(x, y, z)
	if x <= y then
		return y
	else 
		return tarai(tarai((x-1), y, z), tarai((y-1), z, x), tarai((z-1), x, y))
	end
end

###実行
puts tarai(13, 5, 0)
puts Benchmark.measure { 
tarai(13, 5, 0)
}
