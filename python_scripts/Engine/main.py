import pygame
import pygame.locals
import Tile as tile
import GameObject
import KeyInput
import random

p = GameObject.GameObject()
p.__init__()
t = []
bat = GameObject.GameObject()
bat.__init__()

def generate_tiles(w,h, list):
    for x in range(0, w):
        tx = []
        for y in range(0, h):
            tx.append(tile.Tile())
            tx[y].pos = [x*32,y*32]
            tx[y].col = [random.randint(0,255),random.randint(0,255),random.randint(0,255)]
        list.append(tx)

def load_tileset(filename, width, height, scale):
    image = pygame.image.load(filename).convert_alpha()
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
    #print(len(tile_table))
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
    #p.pos[0] += p.vel[0]
    #p.pos[1] += p.vel[1]

def object_orientation(obj):
    if (obj.vel[0] > 0):
        obj.orientation = obj.right
    elif (obj.vel[0] < 0):
        obj.orientation = obj.left
    if (obj.vel[1] > 0):
        obj.orientation = obj.down
    elif (obj.vel[1] < 0):
        obj.orientation = obj.up

def object_animation(obj,time):
    if obj.time >= time:
        obj.time = 0
        if obj.vel[1] != 0 or obj.vel[0] != 0 or obj.running:
            obj.mov_animation += 1
            if obj.mov_animation == len(obj.img) : obj.mov_animation = 0



if __name__ == '__main__':
    pygame.init()
    width = 640
    height = 480
    clock = pygame.time.Clock()
    playtime = 0.0
    FPS = 60
    gameloop = True
    time = 0
    screen = pygame.display.set_mode((width, height))

    #############LOAD IMAGE TILESET FOR GAMEOBJECT #############

    p.img = load_tileset('link.jpg', 64, 96, 0.5)
    bat.img = load_tileset('bat.png', 32,32,1.5)

    ############################################################
    bat.running = True
    p.up, p.down, p.left, p.right = 2,3,0,1
    bat.up, bat.down, bat.left, bat.right = 2, 0, 3, 1
    keys = [False, False, False, False]         #List of which movement key is pressed

    generate_tiles(width//32,height // 32, t)
    while gameloop:
        milliseconds = clock.tick(FPS)
        playtime += milliseconds / 1000.0

        # Print framerate and playtime in titlebar.
        text = "FPS: {0:.2f}   Playtime: {1:.2f}".format(clock.get_fps(), playtime)
        pygame.display.set_caption(text)

        #Drawing all the tiles
        for i in t:
            for j in i:
                pygame.draw.rect(screen, (j.col[0], j.col[1], j.col[2]), (j.pos[0], j.pos[1], 32, 32))

        ################### DRAW THE SCREEN ELEMENTS #################

        screen.blit(p.img[p.mov_animation][p.orientation], p.pos)
        screen.blit(bat.img[bat.mov_animation][bat.orientation], bat.pos)

        ############################################################

        #update the screen
        pygame.display.flip()
        # loop through the events
        for event in pygame.event.get():
            # check if the event is the X button
            if event.type == pygame.QUIT:
                # if it is quit the game
                pygame.quit()
                exit(0)

            KeyInput.KeyCheck(event,keys) #Key events
        movement()

        ######## CALL UPDATE FUNCTION FOR GAMEOBJECTS ########

        p.update()
        bat.update()

        ######################################################

        object_orientation(p)
        object_orientation(bat)

        p.time +=milliseconds
        bat.time +=milliseconds
        object_animation(p, 80)
        object_animation(bat, 130)