import GameObject
import FSM
import math
class Bat(GameObject.GameObject):
    def __init__(self, x, y, w, h, img):
        super(Bat, self).__init__(x,y,w,h)
        self.playerRange = 0
        self.target = [0,0]
        self.agressive = True
        self.aggro = False
        self.aggroRadius = 30
        self.inRange = False
        self.sightOfTarget = False
        self.threat = 0
        self.brain = FSM.FSM(self.idle)
        self.speed = 3
        self.img = img

    def idle(self):
        #print(self.playerRange)
        if(self.playerRange > 300):
            self.brain.pushState(self.chase)

    def chase(self):
        targetX = self.target[0] - self.pos[0]
        targetY = self.target[1] - self.pos[1]
        rotation = math.atan2(targetY, targetX) * 180 / math.pi
        vx = self.speed * (90 - math.fabs(rotation)) / 90
        vy = math.fabs(vx) - self.speed if rotation < 0 else self.speed - math.fabs(vx)
        self.vel[0] = vx
        self.vel[1] = vy

        if(self.playerRange < 80):
            self.vel = [0,0]
            self.brain.popState()

    #def attack(self):

    def update(self, player):
        self.pos[0] += self.vel[0]
        self.pos[1] += self.vel[1]
        self.target = player.pos
        self.playerRange = math.sqrt(math.pow((self.target[0] - self.pos[0]), 2) +
                                     math.pow((self.target[1] - self.pos[1]), 2))
        self.brain.update()

    def anim(self,time):
        if self.time >= time:
            self.time = 0
            self.mov_animation += 1
            if self.mov_animation == len(self.img): self.mov_animation = 0

