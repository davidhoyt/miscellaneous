#!/usr/bin/env ruby

require '../lib/fibonacci-gem.rb'

def main
	values    = [-100, -1, 0, 1, 144, 156, 99, 2000000]
	#expected = [   0,  0, 0, 1, 144, 144, 89, 1346269]
	
	values.each do |value|
		puts "#{value}.closest_fibonacci => #{value.closest_fibonacci}"
	end
end

main
