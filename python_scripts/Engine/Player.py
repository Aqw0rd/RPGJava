import GameObject

class Player(GameObject.GameObject):
    def __init__(self,x,y,w,h):
        super(Player, self).__init__(x,y,w,h)

    def update(self):
        self.pos[0] += self.vel[0]
        self.pos[1] += self.vel[1]

    def anim(self,time):
        if self.time >= time:
            self.time = 0
            if self.vel[1] != 0 or self.vel[0] != 0 or self.running:
                self.mov_animation += 1
                if self.mov_animation == len(self.img): self.mov_animation = 0

    def movement(self,keys):
        if keys[0]:  # UP
            self.vel[1] = -3
        elif keys[2]:  # DOWN
            self.vel[1] = 3
        if keys[1]:  # LEFT
            self.vel[0] = -3
        elif keys[3]:  # RIGHT
            self.vel[0] = 3
        self.vel[0] = 0 if not keys[1] and not keys[3] else self.vel[0]
        self.vel[1] = 0 if not keys[0] and not keys[2] else self.vel[1]