class GameObject:
    def __init__(self):
        self.vel = [0, 0]
        self.pos = [100, 100]
        self.orientation = 0
        self.mov_animation = 0
        self.running = False
        self.up = 0
        self.down = 0
        self.left = 0
        self.right = 0
        self.time = 0
        self.img = ""

    def object_orientation(self):
        if (self.vel[0] > 0):
            self.orientation = self.right
        elif (self.vel[0] < 0):
            self.orientation = self.left
        if (self.vel[1] > 0):
            self.orientation = self.down
        elif (self.vel[1] < 0):
            self.orientation = self.up
