import GameObject, Bat, random
class BatSpawner(GameObject.GameObject):
    def __init__(self, x, y, w, h, img):
        super(BatSpawner, self).__init__(x,y,w,h)
        self.radius = 150
        self.maxEntities = 2
        self.entities = []
        self.respawnTime = 5000
        self.img = img
        for i in range(self.maxEntities):
            self.entities.append(Bat.Bat(random.randint(self.pos[0] - self.radius // 2, self.pos[0] + self.radius // 2),
                                         random.randint(self.pos[1] - self.radius // 2, self.pos[1] + self.radius // 2),
                                         32, 32, self.img))

    def update(self):
        if len(self.entities) < self.maxEntities:
            if self.time >= self.respawnTime:
                self.time = 0
                self.entities.append(Bat.Bat(random.randint(self.pos[0] - self.radius // 2, self.pos[0] + self.radius // 2), random.randint(self.pos[1] - self.radius // 2, self.pos[1] + self.radius // 2), 32, 32, self.img))
        else:
            self.time = 0