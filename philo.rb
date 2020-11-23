require 'thread'

def eating_philosophier
  ph = Array.new(5) {|i|
    x = Philosophier.new(i, nil, nil, nil, nil)
    x.left_fork = Fork.new(true, x)
    x
  }
  ph.size.times do |i|
    ph[i].left  = ph[(i + ph.size - 1) % ph.size]
    ph[i].right = ph[(i + 1) % ph.size]
    ph[i].right_fork =ph[i].right.left_fork
  end
  Array.new(5) {|i|
    Thread.new do
      puts "No. #{i} thinks"
      3.times do
        sleep(0.1 * (rand(5) + 1))
        ph[i].pickup
        ph[i].eat
        ph[i].putdown
      end
    end
  }.each {|t| t.join }
end

class Philosophier
  MUTEX = Mutex.new
  attr_accessor :id, :left, :right, :left_fork, :right_fork, :queue

  def initialize(i, b, f, bf, ff)
    @id, @left, @right, @left_fork, @right_fork = i, b, f, bf, ff
    @queue = Queue.new
  end

  def pickup
    if MUTEX.synchronize { @left_fork.pickup(self) | @right_fork.pickup(self) }
      @queue.pop                                #  ^ ここを || にしないこと
    end
    self
  end

  def eat
    MUTEX.synchronize { puts "No. #{@id} eats" }
    sleep(0.1)
    MUTEX.synchronize { puts "No. #{@id} thinks" }
    self
  end

  def putdown
    MUTEX.synchronize {
      if @left_fork.putdown && @left.own?
        @left.queue.push(nil)
      end
      if @right_fork.putdown && @right.own?
        @right.queue.push(nil)
      end
    }
    self
  end

  def own?
    @left_fork.owner.equal?(self) && @right_fork.owner.equal?(self)
  end
end

class Fork
  attr_accessor :dirty, :owner, :request
  def initialize(a, b)
    @dirty, @owner, @request = a, b, nil
  end

  def pickup(ph)
    if not @dirty and not @owner.equal?(ph)
      @request = ph
      true  # needs to wait
    else
      @dirty = false
      @owner = ph
      @request = nil
      false
    end
  end

  def putdown
    if @request.nil?
      @dirty = true
      false
    else
      @dirty = false
      @owner = @request
      @request = nil
      true  # needs to signal
    end
  end
end

