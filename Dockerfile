FROM sbtscala/scala-sbt:eclipse-temurin-jammy-22_36_1.10.0_3.4.2

# Install additional dependencies for JavaFX and GUI support
RUN apt-get update && \
    apt-get install -y \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libgl1-mesa-glx \
    libgtk-3-0 \
    libgl1-mesa-dri \
    libgl1-mesa-dev \
    libcanberra-gtk-module \
    libcanberra-gtk3-module \
    default-jdk \
    x11-apps \
    xvfb \
    xfonts-base \
    xfonts-100dpi \
    xfonts-75dpi \
    xfonts-scalable \
    xfonts-cyrillic

# Set the working directory inside the container
WORKDIR /blackjack

# Add the project files to the working directory
ADD . /blackjack

# Set the DISPLAY environment variable for GUI applications
ENV DISPLAY=:99

# Run sbt and start the application with Xvfb
CMD Xvfb :99 -screen 0 1024x768x24 > /dev/null 2>&1 & \
    sbt -Djava.awt.headless=false -Dawt.useSystemAAFontSettings=lcd -Dsun.java2d.xrender=true run
