import pygame
import pygame.locals


class GameObject():
    vel = [0, 0]
    pos = [100, 100]
    orientation = 0
    mov_animation = 0
    running = False

class Tile():
    pos = []
    obstacle = False
    col = []



p = GameObject()
t = []

def generate_tiles(w,h, list):
    for x in range(0, w):
        tx = []
        for y in range(0, h):
            tx.append(Tile())
            tx[y].pos = [x*32,y*32]
            tx[y].col = [255,255,255]
        list.append(tx)

def load_tileset(filename, width, height, scale):
    image = pygame.image.load(filename).convert()
    image = pygame.transform.scale(image, (int(image.get_width()*scale), int(image.get_height()*scale)))
    image_width = image.get_width()
    image_height = image.get_height()
    tile_table = []
    for tile_x in range(0, image_width // int(width * scale)):
        line = []
        tile_table.append(line)
        for tile_y in range(0, image_height // int(height * scale)):
            rect = (tile_x * int(width * scale), tile_y * int(height * scale), int(width * scale), int(height * scale))
            line.append(image.subsurface(rect))
    #print(len(tile_table[7]))
    return tile_table

def movement():
    if keys[0]:# UP
        p.vel[1] = -3
    elif keys[2]:#DOWN
        p.vel[1] = 3
    if keys[1]:#LEFT
        p.vel[0] = -3
    elif keys[3]:#RIGHT
        p.vel[0] = 3
    p.vel[0] = 0 if not keys[1] and not keys[3] else p.vel[0]
    p.vel[1] = 0 if not keys[0] and not keys[2] else p.vel[1]
    p.pos[0] += p.vel[0]
    p.pos[1] += p.vel[1]

def player_orientation():
    if (p.vel[0] > 0):
        p.orientation = 1
    elif (p.vel[0] < 0):
        p.orientation = 0
    if (p.vel[1] > 0):
        p.orientation = 3
    elif (p.vel[1] < 0):
        p.orientation = 2

def object_animation(obj):
    obj.mov_animation += 1
    if obj.mov_animation > 7 : obj.mov_animation = 0
    if obj.vel[1] == 0 and obj.vel[0] == 0:
        if obj.orientation == 0:
            obj.mov_animation = 4
        elif obj.orientation == 1:
            obj.mov_animation = 0
        elif obj.orientation == 2:
            obj.mov_animation = 0
        elif obj.orientation == 3:
            obj.mov_animation = 5


if __name__ == '__main__':
    pygame.init()
    width = 640
    height = 480
    clock = pygame.time.Clock()
    playtime = 0.0
    FPS = 60
    gameloop = True
    anim = 0
    screen = pygame.display.set_mode((width, height))
    p.img = load_tileset('link.jpg', 64, 96, 0.5)
    keys = [False, False, False, False]
    generate_tiles(width//32,height // 32, t)
    while gameloop:
        milliseconds = clock.tick(FPS)
        playtime += milliseconds / 1000.0
        # Print framerate and playtime in titlebar.
        text = "FPS: {0:.2f}   Playtime: {1:.2f}".format(clock.get_fps(), playtime)
        pygame.display.set_caption(text)
        #clear the screen before drawing it again
        for i in t:
            for j in i:
                pygame.draw.rect(screen, (j.col[0], j.col[1], j.col[2]), (j.pos[0], j.pos[1], 32, 32))
        #draw the screen elements
        screen.blit(p.img[p.mov_animation][p.orientation], p.pos)
        #update the screen
        pygame.display.flip()
        # loop through the events
        for event in pygame.event.get():
            # check if the event is the X button
            if event.type == pygame.QUIT:
                # if it is quit the game
                pygame.quit()
                exit(0)
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_w:
                    keys[0] = True
                elif event.key == pygame.K_a:
                    keys[1] = True
                elif event.key == pygame.K_s:
                    keys[2] = True
                elif event.key ==pygame. K_d:
                    keys[3] = True
            if event.type == pygame.KEYUP:
                if event.key == pygame.K_w:
                    keys[0] = False
                elif event.key == pygame.K_a:
                    keys[1] = False
                elif event.key == pygame.K_s:
                    keys[2] = False
                elif event.key == pygame.K_d:
                    keys[3] = False
        movement()

        player_orientation()
        anim += milliseconds
        if(anim >= 80):
            object_animation(p)
            anim = 0