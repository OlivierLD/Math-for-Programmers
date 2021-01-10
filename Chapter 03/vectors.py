from math import sqrt, sin, cos, acos, atan2


# def add(v1,v2):
#     return (v1[0] + v2[0], v1[1] + v2[1])

# def add(*vectors):
#     by_coordinate = zip(*vectors)
#     coordinate_sums = [sum(coords) for coords in by_coordinate]
#     return tuple(coordinate_sums)

def add(*vectors):
    return tuple(map(sum, zip(*vectors)))


def subtract(v1, v2):
    return tuple(v1 - v2 for (v1, v2) in zip(v1, v2))


def length(v):
    return sqrt(sum([coord ** 2 for coord in v]))


def dot(u, v):
    return sum([coord1 * coord2 for coord1, coord2 in zip(u, v)])


def distance(v1, v2):
    return length(subtract(v1, v2))


def perimeter(vectors):
    distances = [distance(vectors[i], vectors[(i + 1) % len(vectors)])
                 for i in range(0, len(vectors))]
    return sum(distances)


def scale(scalar, v):
    return tuple(scalar * coord for coord in v)


def to_cartesian(polar_vector):
    length, angle = polar_vector[0], polar_vector[1]
    return (length * cos(angle), length * sin(angle))


def rotate(angle, vectors):
    polars = [to_polar(v) for v in vectors]
    return [to_cartesian((l, a + angle)) for l, a in polars]


def translate(translation, vectors):
    return [add(translation, v) for v in vectors]


def to_polar(vector):
    x, y = vector[0], vector[1]
    angle = atan2(y, x)
    return (length(vector), angle)


def angle_between(v1, v2):
    return acos(
        dot(v1, v2) /
        (length(v1) * length(v2))
    )


def cross(u, v):
    ux, uy, uz = u
    vx, vy, vz = v
    return (uy * vz - uz * vy, uz * vx - ux * vz, ux * vy - uy * vx)


def component(v, direction):
    return (dot(v, direction) / length(direction))


def unit(v):
    return scale(1. / length(v), v)


if __name__ == "__main__":
    one = (1, 2)
    two = (3, 4)
    dot = dot(one, two)
    print("Dot product: {}".format(dot))  # returns 11
    try:
        angle = angle_between(one, two)
        print("Angle between: {}".format(angle))
    except Exception as ex:
        print("angle_between ex: {}".format(str(ex)))
    try:
        cross = cross(one, two)
        print("Cross Product(2): {}".format(cross))
    except Exception as ex:
        print("cross(2) ex: {}".format(str(ex)))

    try:
        cross = cross((1, 2, 3), (4, 5, 6))
        print("Cross Product(3): {}".format(cross))
    except Exception as ex:
        print("cross(3) ex: {}".format(str(ex)))

    try:
        unit = unit(one)
        print("Unit: {}".format(unit))
    except Exception as ex:
        print("unit ex: {}".format(str(ex)))
    try:
        comp = component(one, two)
        print("component: {}".format(comp))
    except Exception as ex:
        print("comp ex: {}".format(str(ex)))
