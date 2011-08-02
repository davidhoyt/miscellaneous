
class Integer < Numeric
  #This could be enhanced by precalculating a large table and then using a 
  #binary search to find the closest value >= the number. If we reach the end 
  #of the table and a value hasn't been found, then run the normal algorithm 
  #starting with the last 2 numbers in the table.
  def closest_fibonacci
    i = self.to_i
    a,b = 0,1
    
    until b > i
      a,b = b,a+b
    end
    
    a
  end
end